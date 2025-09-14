package com.svym.inventory.service.controller;

import com.svym.inventory.service.dto.ExpenseReportDto;
import com.svym.inventory.service.service.DonationReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/donation-report")
@RequiredArgsConstructor
public class DonationReportController {

    private final DonationReportService donationReportService;

    /**
     * Get donation report for latest available month by location ID
     * If locationId is not provided, returns data for all locations
     */
    @GetMapping
    public ResponseEntity<List<ExpenseReportDto>> getDonationReport(
            @RequestParam(required = false) Long locationId) {

        List<ExpenseReportDto> donationData;

        if (locationId != null) {
            donationData = donationReportService.getDonationReportByLocation(locationId);
        } else {
            donationData = donationReportService.getDonationReportForAllLocations();
        }

        return ResponseEntity.ok(donationData);
    }

    /**
     * Get donation report for specific month and year by location ID
     * If locationId is not provided, returns data for all locations
     */
    @GetMapping("/monthly")
    public ResponseEntity<List<ExpenseReportDto>> getDonationReportByMonth(
            @RequestParam(required = false) Long locationId,
            @RequestParam int month,
            @RequestParam int year) {

        List<ExpenseReportDto> donationData;

        if (locationId != null) {
            donationData = donationReportService.getDonationReportByLocationAndMonth(locationId, month, year);
        } else {
            donationData = donationReportService.getDonationReportForAllLocationsByMonth(month, year);
        }

        return ResponseEntity.ok(donationData);
    }

    /**
     * Get donation report for latest available month by specific location ID
     */
    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<ExpenseReportDto>> getDonationReportByLocationId(
            @PathVariable Long locationId) {

        List<ExpenseReportDto> donationData = donationReportService.getDonationReportByLocation(locationId);
        return ResponseEntity.ok(donationData);
    }

    /**
     * Get all available months/years that have donation data
     * Useful for frontend to know what periods have data available
     */
    @GetMapping("/available-periods")
    public ResponseEntity<List<Object[]>> getAvailableMonthsYears() {
        List<Object[]> availablePeriods = donationReportService.getAvailableMonthsYears();
        return ResponseEntity.ok(availablePeriods);
    }
}
