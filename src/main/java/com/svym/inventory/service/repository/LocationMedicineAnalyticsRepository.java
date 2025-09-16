package com.svym.inventory.service.repository;

import com.svym.inventory.service.entity.LocationMedicineAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationMedicineAnalyticsRepository extends JpaRepository<LocationMedicineAnalytics, Long> {

    @Query("""
        SELECT mt.typeName as name, SUM(lma.totalSpend) as value
        FROM LocationMedicineAnalytics lma
        JOIN MedicineType mt ON lma.medicineTypeId = mt.id
        WHERE lma.locationId = :locationId
        AND lma.month = :month
        AND lma.year = :year
        GROUP BY mt.id, mt.typeName
        ORDER BY SUM(lma.totalSpend) DESC
        """)
    List<Object[]> findExpensesByLocationAndMonth(@Param("locationId") Long locationId,
                                                 @Param("month") Integer month,
                                                 @Param("year") Integer year);

    @Query("""
        SELECT mt.typeName as name, SUM(lma.totalSpend) as value
        FROM LocationMedicineAnalytics lma
        JOIN MedicineType mt ON lma.medicineTypeId = mt.id
        WHERE lma.month = :month
        AND lma.year = :year
        GROUP BY mt.id, mt.typeName
        ORDER BY SUM(lma.totalSpend) DESC
        """)
    List<Object[]> findExpensesByMonth(@Param("month") Integer month,
                                      @Param("year") Integer year);

    @Query("""
        SELECT lma FROM LocationMedicineAnalytics lma
        WHERE lma.locationId = :locationId
        AND lma.medicineTypeId = :medicineTypeId
        AND lma.year = :year
        AND lma.month = :month
        AND lma.week = :week
        """)
    LocationMedicineAnalytics findByLocationMedicineTypeAndPeriod(
        @Param("locationId") Long locationId,
        @Param("medicineTypeId") Long medicineTypeId,
        @Param("year") Integer year,
        @Param("month") Integer month,
        @Param("week") Integer week);

    @Query("""
        SELECT
            mpb.location.id,
            mpb.medicine.type.id,
            SUM(mpb.totalPrice)
        FROM MedicinePurchaseBatch mpb
        WHERE DATE(mpb.createdAt) = :date
        AND mpb.isActive = true
        AND mpb.isDeleted = false
        GROUP BY mpb.location.id, mpb.medicine.type.id
        """)
    List<Object[]> getDailySpendByLocationAndMedicineType(@Param("date") java.time.LocalDate date);
}
