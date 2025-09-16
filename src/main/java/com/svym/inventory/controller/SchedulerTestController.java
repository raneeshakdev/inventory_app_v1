package com.svym.inventory.controller;

import com.svym.inventory.scheduler.expiringmedicine.ExpiringMedicineScheduler;
import com.svym.inventory.scheduler.medicinedailysummary.MedicineDailySummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Test controller for manually triggering schedulers
 */
@RestController
@RequestMapping("/api/test")
public class SchedulerTestController {

    @Autowired
    private ExpiringMedicineScheduler expiringMedicineScheduler;

    @Autowired
    private MedicineDailySummaryService medicineDailySummaryService;

    @PostMapping("/run-expiry-check")
    public ResponseEntity<String> runExpiryCheck() {
        try {
            expiringMedicineScheduler.checkExpiringMedicines();
            return ResponseEntity.ok("Medicine expiry check completed successfully. Check logs for details.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error running expiry check: " + e.getMessage());
        }
    }

    @GetMapping("/scheduler-info")
    public ResponseEntity<Map<String, Object>> getSchedulerInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("schedulerClass", "ExpiringMedicineScheduler");
        info.put("cronExpression", "0 15 0 * * *");
        info.put("description", "Runs every day at 12:15 AM");
        info.put("currentTime", LocalDateTime.now());
        info.put("status", "Scheduler is configured and ready");
        return ResponseEntity.ok(info);
    }

    /**
     * Test API for MedicineDailySummaryScheduler for a specific date
     * POST /api/test/medicine-daily-summary
     * Body: { "date": "2025-09-07" }
     */
    @PostMapping("/medicine-daily-summary")
    public ResponseEntity<Map<String, Object>> testMedicineDailySummaryForDate(@RequestBody Map<String, String> request) {
        try {
            String dateStr = request.get("date");
            if (dateStr == null || dateStr.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Date parameter is required");
                errorResponse.put("format", "YYYY-MM-DD (e.g., 2025-09-07)");
                errorResponse.put("executionTime", LocalDateTime.now());
                return ResponseEntity.badRequest().body(errorResponse);
            }

            LocalDate summaryDate;
            try {
                summaryDate = LocalDate.parse(dateStr.trim(), DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid date format");
                errorResponse.put("providedDate", dateStr);
                errorResponse.put("expectedFormat", "YYYY-MM-DD (e.g., 2025-09-07)");
                errorResponse.put("executionTime", LocalDateTime.now());
                return ResponseEntity.badRequest().body(errorResponse);
            }

            LocalDate jobRunDate = LocalDate.now();

            // Run the medicine daily summary job for the specified date
            medicineDailySummaryService.generateAndSaveDailySummary(summaryDate, jobRunDate);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Medicine daily summary job completed successfully");
            response.put("summaryDate", summaryDate.toString());
            response.put("jobRunDate", jobRunDate.toString());
            response.put("executionTime", LocalDateTime.now());
            response.put("status", "SUCCESS");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to run medicine daily summary job");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("executionTime", LocalDateTime.now());
            errorResponse.put("status", "ERROR");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping("/run-daily-summary-last-40-days")
    public ResponseEntity<Map<String, Object>> runDailySummaryLast40Days() {
        try {
            LocalDate endDate = LocalDate.now().minusDays(1); // Yesterday
            LocalDate startDate = endDate.minusDays(39); // 40 days ago
            LocalDate jobRunDate = LocalDate.now();

            List<String> processedDates = new ArrayList<>();
            List<String> errorDates = new ArrayList<>();

            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                try {
                    medicineDailySummaryService.generateAndSaveDailySummary(date, jobRunDate);
                    processedDates.add(date.toString());
                } catch (Exception e) {
                    errorDates.add(date.toString() + " - Error: " + e.getMessage());
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Daily summary job completed for last 40 days");
            response.put("startDate", startDate.toString());
            response.put("endDate", endDate.toString());
            response.put("totalDaysProcessed", processedDates.size());
            response.put("totalErrors", errorDates.size());
            response.put("processedDates", processedDates);
            response.put("errorDates", errorDates);
            response.put("executionTime", LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to run daily summary for last 40 days");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("executionTime", LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping("/run-daily-summary-single-day")
    public ResponseEntity<Map<String, Object>> runDailySummarySingleDay() {
        try {
            LocalDate summaryDate = LocalDate.now().minusDays(1); // Yesterday
            LocalDate jobRunDate = LocalDate.now();

            medicineDailySummaryService.generateAndSaveDailySummary(summaryDate, jobRunDate);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Medicine daily summary job completed successfully for yesterday");
            response.put("summaryDate", summaryDate.toString());
            response.put("jobRunDate", jobRunDate.toString());
            response.put("executionTime", LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to run daily summary for yesterday");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("executionTime", LocalDateTime.now());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
