package com.svym.inventory.service.medicinedailycostsummary;

import com.svym.inventory.service.dto.MedicineDailyCostSummaryDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for managing medicine daily cost summary operations.
 * Provides methods for CRUD operations and business logic related to daily cost summaries.
 */
public interface MedicineDailyCostSummaryService {

    /**
     * Create a new medicine daily cost summary
     */
    MedicineDailyCostSummaryDTO create(MedicineDailyCostSummaryDTO dto);

    /**
     * Update an existing medicine daily cost summary
     */
    MedicineDailyCostSummaryDTO update(Long id, MedicineDailyCostSummaryDTO dto);

    /**
     * Delete a medicine daily cost summary by ID
     */
    void delete(Long id);

    /**
     * Get a medicine daily cost summary by ID
     */
    MedicineDailyCostSummaryDTO getById(Long id);

    /**
     * Get all medicine daily cost summaries
     */
    List<MedicineDailyCostSummaryDTO> getAll();

    /**
     * Get summary by medicine, location and distribution date
     */
    MedicineDailyCostSummaryDTO getByMedicineLocationAndDate(Long medicineId, Long locationId, LocalDate distDate);

    /**
     * Get summary by medicine, location, delivery center and distribution date
     */
    MedicineDailyCostSummaryDTO getByMedicineLocationDeliveryCenterAndDate(Long medicineId, Long locationId, Long deliveryCenterId, LocalDate distDate);

    /**
     * Get all summaries for a specific date
     */
    List<MedicineDailyCostSummaryDTO> getByDate(LocalDate distDate);

    /**
     * Get all summaries within a date range
     */
    List<MedicineDailyCostSummaryDTO> getByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Get all summaries for a specific medicine
     */
    List<MedicineDailyCostSummaryDTO> getByMedicine(Long medicineId);

    /**
     * Get all summaries for a specific location
     */
    List<MedicineDailyCostSummaryDTO> getByLocation(Long locationId);

    /**
     * Get all summaries for a specific medicine and location
     */
    List<MedicineDailyCostSummaryDTO> getByMedicineAndLocation(Long medicineId, Long locationId);

    /**
     * Get summaries for a specific medicine within date range
     */
    List<MedicineDailyCostSummaryDTO> getByMedicineAndDateRange(Long medicineId, LocalDate startDate, LocalDate endDate);

    /**
     * Get summaries for a specific location within date range
     */
    List<MedicineDailyCostSummaryDTO> getByLocationAndDateRange(Long locationId, LocalDate startDate, LocalDate endDate);

    /**
     * Get total cost for a medicine at a location within date range
     */
    Double getTotalCostByMedicineLocationAndDateRange(Long medicineId, Long locationId, LocalDate startDate, LocalDate endDate);

    /**
     * Get total units distributed for a medicine at a location within date range
     */
    Integer getTotalUnitsDistributedByMedicineLocationAndDateRange(Long medicineId, Long locationId, LocalDate startDate, LocalDate endDate);

    /**
     * Create or update daily cost summary for a medicine distribution
     * This method will either create a new record or update existing one for the same medicine, location, delivery center and date
     */
    MedicineDailyCostSummaryDTO createOrUpdateSummary(Long medicineId, Long locationId, Long deliveryCenterId, LocalDate distDate, Integer numberOfUnit, Double totalPrice);

    /**
     * Create or update daily cost summary for a medicine distribution (legacy method - kept for compatibility)
     * This method will either create a new record or update existing one for the same medicine, location and date
     */
    MedicineDailyCostSummaryDTO createOrUpdateSummary(Long medicineId, Long locationId, LocalDate distDate, Integer numberOfUnit, Double totalPrice);

    /**
     * Get daily cost summaries for a location within date range ordered by date and cost
     */
    List<MedicineDailyCostSummaryDTO> getByLocationAndDateRangeOrderByDateAndCost(Long locationId, LocalDate startDate, LocalDate endDate);
}
