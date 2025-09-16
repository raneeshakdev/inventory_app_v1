package com.svym.inventory.service.repository;

import com.svym.inventory.service.entity.LocationAnalytics;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LocationAnalyticsRepository extends CrudRepository<LocationAnalytics, Long> {

    @Query("SELECT la.monthName, SUM(la.weeklyTotalPurchase) as totalAmount " +
           "FROM LocationAnalytics la " +
           "WHERE la.locationId = :locationId AND la.yearNumber = :year " +
           "GROUP BY la.monthName, la.yearNumber " +
           "ORDER BY " +
           "CASE la.monthName " +
           "WHEN 'Jan' THEN 1 " +
           "WHEN 'Feb' THEN 2 " +
           "WHEN 'Mar' THEN 3 " +
           "WHEN 'Apr' THEN 4 " +
           "WHEN 'May' THEN 5 " +
           "WHEN 'Jun' THEN 6 " +
           "WHEN 'Jul' THEN 7 " +
           "WHEN 'Aug' THEN 8 " +
           "WHEN 'Sep' THEN 9 " +
           "WHEN 'Oct' THEN 10 " +
           "WHEN 'Nov' THEN 11 " +
           "WHEN 'Dec' THEN 12 " +
           "END")
    List<Object[]> findMonthlyTotalsByLocationAndYear(@Param("locationId") Long locationId, @Param("year") Integer year);

    @Query("SELECT SUM(la.weeklyTotalPurchase) " +
           "FROM LocationAnalytics la " +
           "WHERE la.locationId = :locationId AND la.monthName = :monthName AND la.yearNumber = :year")
    BigDecimal findMonthlyTotalByLocationAndMonth(@Param("locationId") Long locationId,
                                                  @Param("monthName") String monthName,
                                                  @Param("year") Integer year);

    @Query("SELECT la FROM LocationAnalytics la " +
           "WHERE la.locationId = :locationId " +
           "AND la.yearNumber = :year " +
           "AND la.weekNumber = :week " +
           "AND la.monthName = :monthName")
    LocationAnalytics findByLocationAndPeriod(@Param("locationId") Long locationId,
                                            @Param("year") Integer year,
                                            @Param("week") Integer week,
                                            @Param("monthName") String monthName);

    @Query("SELECT " +
           "mpb.location.id, " +
           "SUM(mpb.totalPrice) " +
           "FROM MedicinePurchaseBatch mpb " +
           "WHERE DATE(mpb.createdAt) = :date " +
           "AND mpb.isActive = true " +
           "AND mpb.isDeleted = false " +
           "GROUP BY mpb.location.id")
    List<Object[]> getDailyPurchaseByLocation(@Param("date") java.time.LocalDate date);
}
