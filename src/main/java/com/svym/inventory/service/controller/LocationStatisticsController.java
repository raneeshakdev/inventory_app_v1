package com.svym.inventory.service.controller;

import com.svym.inventory.service.dto.LocationStatisticsDto;
import com.svym.inventory.service.service.LocationStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/location-statistics")
@RequiredArgsConstructor
public class LocationStatisticsController {

    private final LocationStatisticsService locationStatisticsService;

    @GetMapping
    public ResponseEntity<List<LocationStatisticsDto>> getAllLocationStatistics() {
        List<LocationStatisticsDto> statistics = locationStatisticsService.getAllLocationStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<LocationStatisticsDto> getLocationStatistics(@PathVariable Long locationId) {
        Optional<LocationStatisticsDto> statistics = locationStatisticsService.getLocationStatisticsById(locationId);
        return statistics.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/locations-with-statistics")
    public ResponseEntity<List<LocationStatisticsDto>> getAllLocationsWithStatistics() {
        List<LocationStatisticsDto> locationsWithStats = locationStatisticsService.getAllLocationsWithStatistics();
        return ResponseEntity.ok(locationsWithStats);
    }

    @PutMapping("/location/{locationId}")
    public ResponseEntity<LocationStatisticsDto> updateLocationStatistics(
            @PathVariable Long locationId,
            @RequestParam Integer stockStatusCount,
            @RequestParam Integer expiredCount,
            @RequestParam Integer nearExpiryCount) {

        LocationStatisticsDto updatedStats = locationStatisticsService.updateLocationStatistics(
                locationId, stockStatusCount, expiredCount, nearExpiryCount);
        return ResponseEntity.ok(updatedStats);
    }
}
