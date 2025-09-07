package com.svym.inventory.service.controller;

import com.svym.inventory.service.dto.MedicineDailyCostSummaryDTO;
import com.svym.inventory.service.medicinedailycostsummary.MedicineDailyCostSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for Medicine Daily Cost Summary operations.
 * Provides endpoints for retrieving medicine distribution cost data with dynamic filtering.
 */
@RestController
@RequestMapping("/api/medicine-daily-cost-summary")
@CrossOrigin(origins = "*")
public class MedicineDailyCostSummaryController {

    @Autowired
    private MedicineDailyCostSummaryService service;

    /**
     * Get data filtered by location ID and date range
     *
     * @param locationId Location ID (required)
     * @param startDate Start date (required, format: yyyy-MM-dd)
     * @param endDate End date (required, format: yyyy-MM-dd)
     * @return List of medicine daily cost summary data for the location within date range
     */
    @GetMapping("/location/{locationId}/date-range")
    public ResponseEntity<List<MedicineDailyCostSummaryDTO>> getByLocationAndDateRange(
            @PathVariable Long locationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<MedicineDailyCostSummaryDTO> results = service.getByLocationAndDateRange(locationId, startDate, endDate);
        return ResponseEntity.ok(results);
    }

    /**
     * Get data filtered by date range only
     *
     * @param startDate Start date (required, format: yyyy-MM-dd)
     * @param endDate End date (required, format: yyyy-MM-dd)
     * @return List of medicine daily cost summary data within date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<MedicineDailyCostSummaryDTO>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<MedicineDailyCostSummaryDTO> results = service.getByDateRange(startDate, endDate);
        return ResponseEntity.ok(results);
    }

    /**
     * Get all data (use with caution on large datasets)
     *
     * @return List of all medicine daily cost summary data
     */
    @GetMapping("/all")
    public ResponseEntity<List<MedicineDailyCostSummaryDTO>> getAllData() {
        List<MedicineDailyCostSummaryDTO> results = service.getAll();
        return ResponseEntity.ok(results);
    }

    /**
     * Get data filtered by location ID only
     *
     * @param locationId Location ID (required)
     * @return List of medicine daily cost summary data for the location
     */
    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<MedicineDailyCostSummaryDTO>> getByLocation(@PathVariable Long locationId) {
        List<MedicineDailyCostSummaryDTO> results = service.getByLocation(locationId);
        return ResponseEntity.ok(results);
    }

    /**
     * Get medicine daily cost summary data with dynamic filtering
     * This uses existing service methods to simulate dynamic filtering
     *
     * @param locationId Optional location ID filter
     * @param startDate Optional start date for date range filter (format: yyyy-MM-dd)
     * @param endDate Optional end date for date range filter (format: yyyy-MM-dd)
     * @return List of filtered medicine daily cost summary data
     */
    @GetMapping("/filter")
    public ResponseEntity<List<MedicineDailyCostSummaryDTO>> getFilteredData(
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<MedicineDailyCostSummaryDTO> results;

        if (locationId != null && startDate != null && endDate != null) {
            // Filter by location and date range
            results = service.getByLocationAndDateRange(locationId, startDate, endDate);
        } else if (startDate != null && endDate != null) {
            // Filter by date range only
            results = service.getByDateRange(startDate, endDate);
        } else if (locationId != null) {
            // Filter by location only
            results = service.getByLocation(locationId);
        } else {
            // Return all data
            results = service.getAll();
        }

        return ResponseEntity.ok(results);
    }

    /**
     * Get medicine daily cost summary data with dynamic filtering using POST method
     * Accepts filtering criteria in request body with flexible date handling
     *
     * @param filterRequest Request body containing filter criteria
     * @return List of filtered medicine daily cost summary data
     */
    @PostMapping("/search")
    public ResponseEntity<List<MedicineDailyCostSummaryDTO>> searchWithFilters(
            @RequestBody MedicineDailyCostSummaryFilterRequest filterRequest) {

        List<MedicineDailyCostSummaryDTO> results;

        List<Long> locationIds = filterRequest.getLocationIds();
        Long deliveryCenterId = filterRequest.getDeliveryCenterId();
        LocalDate startDate = filterRequest.getStartDate();
        LocalDate endDate = filterRequest.getEndDate();

        // Handle single date scenario - if only one date is provided, use it for both start and end
        if (startDate != null && endDate == null) {
            endDate = startDate;
        } else if (endDate != null && startDate == null) {
            startDate = endDate;
        }

        // Use the service's dynamic filtering method
        results = service.getViewDataWithFilters(locationIds, deliveryCenterId, startDate, endDate);

        return ResponseEntity.ok(results);
    }
}

/**
 * Request class for POST filtering of Medicine Daily Cost Summary data
 */
class MedicineDailyCostSummaryFilterRequest {
    private List<Long> locationIds;
    private Long deliveryCenterId;
    private LocalDate startDate;
    private LocalDate endDate;

    // Default constructor
    public MedicineDailyCostSummaryFilterRequest() {}

    // Constructor with all parameters
    public MedicineDailyCostSummaryFilterRequest(List<Long> locationIds, Long deliveryCenterId,
                                                LocalDate startDate, LocalDate endDate) {
        this.locationIds = locationIds;
        this.deliveryCenterId = deliveryCenterId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public List<Long> getLocationIds() {
        return locationIds;
    }

    public void setLocationIds(List<Long> locationIds) {
        this.locationIds = locationIds;
    }

    public Long getDeliveryCenterId() {
        return deliveryCenterId;
    }

    public void setDeliveryCenterId(Long deliveryCenterId) {
        this.deliveryCenterId = deliveryCenterId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
