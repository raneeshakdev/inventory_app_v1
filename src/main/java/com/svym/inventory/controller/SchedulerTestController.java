package com.svym.inventory.controller;

import com.svym.inventory.scheduler.expiringmedicine.ExpiringMedicineScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Test controller for manually triggering the medicine expiry scheduler
 */
@RestController
@RequestMapping("/api/test")
public class SchedulerTestController {

    @Autowired
    private ExpiringMedicineScheduler expiringMedicineScheduler;

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
}
