package com.svym.inventory.service.controller;

import com.svym.inventory.service.dto.ExpenseReportDto;
import com.svym.inventory.service.service.ExpenseReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/expense-report")
@RequiredArgsConstructor
public class ExpenseReportController {

    private final ExpenseReportService expenseReportService;

    /**
     * Get expense report for current month by location ID
     * If locationId is not provided, returns data for all locations
     */
    @GetMapping
    public ResponseEntity<List<ExpenseReportDto>> getExpenseReport(
            @RequestParam(required = false) Long locationId) {

        List<ExpenseReportDto> expenseData;

        if (locationId != null) {
            expenseData = expenseReportService.getExpenseReportByLocation(locationId);
        } else {
            expenseData = expenseReportService.getExpenseReportForAllLocations();
        }

        return ResponseEntity.ok(expenseData);
    }

    /**
     * Get expense report for specific month and year by location ID
     * If locationId is not provided, returns data for all locations
     */
    @GetMapping("/monthly")
    public ResponseEntity<List<ExpenseReportDto>> getExpenseReportByMonth(
            @RequestParam(required = false) Long locationId,
            @RequestParam int month,
            @RequestParam int year) {

        List<ExpenseReportDto> expenseData;

        if (locationId != null) {
            expenseData = expenseReportService.getExpenseReportByLocationAndMonth(locationId, month, year);
        } else {
            expenseData = expenseReportService.getExpenseReportForAllLocationsByMonth(month, year);
        }

        return ResponseEntity.ok(expenseData);
    }

    /**
     * Get expense report for current month by specific location ID
     */
    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<ExpenseReportDto>> getExpenseReportByLocationId(
            @PathVariable Long locationId) {

        List<ExpenseReportDto> expenseData = expenseReportService.getExpenseReportByLocation(locationId);
        return ResponseEntity.ok(expenseData);
    }
}
