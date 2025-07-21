package com.svym.inventory.service.medicinestock;

import com.svym.inventory.service.entity.Medicine;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MedicineStockRepository {

    private final EntityManager em;

    public Page<MedicineStockRowDTO> findStockForLocation(
            Long locationId,
            String search,
            int nearExpiryDays,
            Pageable pageable) {

        LocalDateTime nearExpiryLimit = LocalDateTime.now().plusDays(nearExpiryDays);

        String jpql = """
            SELECT new com.svym.inventory.service.medicinestock.MedicineStockRowDTO(
                m.id,
                m.name,
                m.code,
                mt.typeName,
                COUNT(b.id),
                COALESCE(MAX(b.lastModifiedBy), m.lastModifiedBy),
                COALESCE(MAX(b.lastUpdatedAt), m.lastModifiedAt),
                /* dummy placeholders – we calculate after fetch */ null,
                null,
                null,
                null
            )
            FROM Medicine m
            JOIN m.purchaseBatches b
            JOIN m.type mt
            WHERE b.location.id = :loc
              AND ( :search IS NULL
                    OR LOWER(m.name) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR LOWER(m.code) LIKE LOWER(CONCAT('%', :search, '%')) )
            GROUP BY m.id, m.name, m.code, mt.typeName
        """;

        TypedQuery<MedicineStockRowDTO> q = em.createQuery(jpql, MedicineStockRowDTO.class);
        q.setParameter("loc", locationId);
        q.setParameter("search", (search == null || search.isBlank()) ? null : search);
        q.setFirstResult((int) pageable.getOffset());
        q.setMaxResults(pageable.getPageSize());

        List<MedicineStockRowDTO> rows = q.getResultList();

        // --- Post‑process to fill derived fields --------------------------------
        rows.replaceAll(row -> enrichRow(row, nearExpiryLimit));

        return new PageImpl<>(rows, pageable, rows.size()); // count query omitted for brevity
    }

    /* helper */
    private MedicineStockRowDTO enrichRow(MedicineStockRowDTO row,
                                          LocalDateTime nearExpiryLimit) {

        // get aggregated quantities per medicine in one shot
        Object[] agg = (Object[]) em.createQuery("""
            SELECT
                SUM(b.currentQuantity),
                SUM(CASE WHEN b.expiryDate < CURRENT_TIMESTAMP THEN b.currentQuantity ELSE 0 END),
                SUM(CASE WHEN b.expiryDate BETWEEN CURRENT_TIMESTAMP AND :limit THEN b.currentQuantity ELSE 0 END)
            FROM MedicinePurchaseBatch b
            WHERE b.medicine.id = :mid
        """)
        .setParameter("mid", row.medicineId())
        .setParameter("limit", nearExpiryLimit)
        .getSingleResult();

        int qty          = ((Number) agg[0]).intValue();
        int expiredQty   = ((Number) agg[1]).intValue();
        int nearExpQty   = ((Number) agg[2]).intValue();

        Medicine med = em.find(Medicine.class, row.medicineId());
        int threshold = med.getStockThreshold();

        /* --- stock status evaluation --- */
        StockStatus stockStatus;
        if (qty == 0)                      stockStatus = StockStatus.OUT_OF_STOCK;
        else if (qty < threshold)          stockStatus = StockStatus.UNDER_STOCK;
        else                               stockStatus = StockStatus.IN_STOCK;

        /* --- expiry evaluation --- */
        ExpiryStatus expiryStatus;
        String expiryLabel;
        if (expiredQty > 0) {
            expiryStatus = ExpiryStatus.EXPIRED;
            expiryLabel  = expiredQty + "/" + qty + " Expired";
        } else if (nearExpQty > 0) {
            expiryStatus = ExpiryStatus.NEAR_EXPIRY;
            expiryLabel  = nearExpQty + "/" + qty + " Near Expiry";
        } else {
            expiryStatus = ExpiryStatus.OK;
            expiryLabel  = "-";
        }

        String stockLabel = qty + "/" + threshold;

        return new MedicineStockRowDTO(
                row.medicineId(), row.medicineName(), row.medicineCode(),
                row.medicineType(), row.noOfBatches(), row.updatedBy(),
                row.updatedDate(), stockStatus, stockLabel,
                expiryStatus, expiryLabel
        );
    }
}
