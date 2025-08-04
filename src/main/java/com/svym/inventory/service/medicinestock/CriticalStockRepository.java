package com.svym.inventory.service.medicinestock;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CriticalStockRepository {

    private final EntityManager em;

    public Page<CriticalStockRowDTO> find(String locationId,
                                          String search,
                                          int nearExpiryDays,
                                          Pageable pageable) {

        LocalDate today           = LocalDate.now();
        LocalDate nearExpiryLimit = today.plusDays(nearExpiryDays);

        /* 1) Collect ONE row per medicine for the location ----------------------- */
        String jpql = """
            SELECT new com.svym.inventory.service.medicinestock.CriticalStockRowDTO(
                 m.id,
                 m.name,
                 COUNT(b.id),                -- batches
                 m.stockThreshold,           -- minimum required
                 null, null                  -- urgency placeholders
            )
            FROM Medicine m
            JOIN m.purchaseBatches b
            WHERE b.location.id = :loc
              AND ( :q IS NULL
                    OR LOWER(m.name) LIKE LOWER(CONCAT('%', :q, '%')) )
            GROUP BY m.id, m.name, m.stockThreshold
        """;

        TypedQuery<CriticalStockRowDTO> q = em.createQuery(jpql, CriticalStockRowDTO.class);
        q.setParameter("loc", Long.valueOf(locationId));
        q.setParameter("q", search == null || search.isBlank() ? null : search);
        q.setFirstResult((int) pageable.getOffset());
        q.setMaxResults(pageable.getPageSize());

        List<CriticalStockRowDTO> rows = q.getResultList();

        /* 2) Enrich each row with totals & decide if it is critical -------------- */
        rows.removeIf(r -> !evaluateAndAmend(r, today, nearExpiryLimit)); // keep only critical rows

        return new PageImpl<>(rows, pageable, rows.size()); // quick win; add count‑query if needed
    }

    /** Calculates quantities + urgency; returns false if row is NOT critical. */
    private boolean evaluateAndAmend(CriticalStockRowDTO row,
                                     LocalDate today,
                                     LocalDate nearExpiryLimit) {

        Object[] agg = (Object[]) em.createQuery("""
            SELECT
                SUM(b.currentQuantity),                                         -- 0  total qty
                SUM(CASE WHEN b.expiryDate < :today THEN b.currentQuantity ELSE 0 END)                -- 1  expired
            FROM MedicinePurchaseBatch b
            WHERE b.medicine.id = :mid
        """)
        .setParameter("today", today)
        .setParameter("mid", row.id())
        .getSingleResult();

        int qty        = ((Number) agg[0]).intValue();
        int expiredQty = ((Number) agg[1]).intValue();

        /* Decide urgency ------------------------------------------------------- */
        UrgencyReason reason  = null;
        String        label   = null;

        if (expiredQty > 0) {
            reason = UrgencyReason.EXPIRED;
            label  = "Expired";
        } else if (qty == 0) {
            reason = UrgencyReason.OUT_OF_STOCK;
            label  = "Out of stock";
        } else if (qty < row.minimumRequired()) {
            reason = UrgencyReason.CRITICALLY_LOW;
            label  = "Critically Low";
        }

        if (reason == null) return false;      // not critical -> drop row

        /* Re‑create record with urgency filled in ------------------------------ */
        row = rowsReplace(row, reason, label); // see helper below
        return true;
    }

    /** tiny helper to rebuild immutable record */
    private CriticalStockRowDTO rowsReplace(CriticalStockRowDTO src,
                                            UrgencyReason reason,
                                            String label) {
        return new CriticalStockRowDTO(
                src.id(), src.medicineName(), src.noOfBatch(),
                src.minimumRequired(), reason, label
        );
    }
}
