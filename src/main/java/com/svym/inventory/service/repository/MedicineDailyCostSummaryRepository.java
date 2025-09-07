package com.svym.inventory.service.repository;

import com.svym.inventory.service.entity.MedicineDailyCostSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineDailyCostSummaryRepository extends JpaRepository<MedicineDailyCostSummary, Long> {

    /**
     * Find summary by medicine, location and distribution date
     */
    Optional<MedicineDailyCostSummary> findByMedicineIdAndLocationIdAndDistDate(
            Long medicineId, Long locationId, LocalDate distDate);

    /**
     * Find all summaries for a specific date
     */
    List<MedicineDailyCostSummary> findByDistDate(LocalDate distDate);

    /**
     * Find all summaries for a specific date range
     */
    List<MedicineDailyCostSummary> findByDistDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Find all summaries for a specific medicine
     */
    List<MedicineDailyCostSummary> findByMedicineId(Long medicineId);

    /**
     * Find all summaries for a specific location
     */
    List<MedicineDailyCostSummary> findByLocationId(Long locationId);

    /**
     * Find all summaries for a specific medicine and location
     */
    List<MedicineDailyCostSummary> findByMedicineIdAndLocationId(Long medicineId, Long locationId);

    /**
     * Find all summaries for a specific medicine within date range
     */
    List<MedicineDailyCostSummary> findByMedicineIdAndDistDateBetween(
            Long medicineId, LocalDate startDate, LocalDate endDate);

    /**
     * Find all summaries for a specific location within date range
     */
    List<MedicineDailyCostSummary> findByLocationIdAndDistDateBetween(
            Long locationId, LocalDate startDate, LocalDate endDate);

    /**
     * Get total cost for a medicine at a location within date range
     */
    @Query("SELECT COALESCE(SUM(m.totalPrice), 0.0) FROM MedicineDailyCostSummary m " +
           "WHERE m.medicineId = :medicineId AND m.locationId = :locationId " +
           "AND m.distDate BETWEEN :startDate AND :endDate")
    Double getTotalCostByMedicineLocationAndDateRange(
            @Param("medicineId") Long medicineId,
            @Param("locationId") Long locationId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * Get total units distributed for a medicine at a location within date range
     */
    @Query("SELECT COALESCE(SUM(m.numberOfUnit), 0) FROM MedicineDailyCostSummary m " +
           "WHERE m.medicineId = :medicineId AND m.locationId = :locationId " +
           "AND m.distDate BETWEEN :startDate AND :endDate")
    Integer getTotalUnitsDistributedByMedicineLocationAndDateRange(
            @Param("medicineId") Long medicineId,
            @Param("locationId") Long locationId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * Get daily cost summary for all medicines at a location within date range
     */
    @Query("SELECT m FROM MedicineDailyCostSummary m " +
           "WHERE m.locationId = :locationId " +
           "AND m.distDate BETWEEN :startDate AND :endDate " +
           "ORDER BY m.distDate DESC, m.totalPrice DESC")
    List<MedicineDailyCostSummary> findByLocationAndDateRangeOrderByDateAndCost(
            @Param("locationId") Long locationId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
