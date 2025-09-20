package com.svym.inventory.service.controller;

import com.svym.inventory.service.dto.MonthlyAnalyticsDto;
import com.svym.inventory.service.service.LocationAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/location-analytics")
@RequiredArgsConstructor
public class LocationAnalyticsController {

    private final LocationAnalyticsService locationAnalyticsService;

    @GetMapping("/monthly/{locationId}")
    public ResponseEntity<List<MonthlyAnalyticsDto>> getMonthlyAnalytics(@PathVariable Long locationId) {
        List<MonthlyAnalyticsDto> monthlyData = locationAnalyticsService.getMonthlyAnalytics(locationId, null);
        return ResponseEntity.ok(monthlyData);
    }

    @GetMapping("/monthly/{locationId}/{year}")
    public ResponseEntity<List<MonthlyAnalyticsDto>> getMonthlyAnalyticsForYear(@PathVariable Long locationId, @PathVariable Integer year) {
        List<MonthlyAnalyticsDto> monthlyData = locationAnalyticsService.getMonthlyAnalytics(locationId, year);
        return ResponseEntity.ok(monthlyData);
    }
}
