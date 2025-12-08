package com.svym.inventory.service.service;

import com.svym.inventory.service.dto.LocationStatisticsDto;
import com.svym.inventory.service.entity.Location;
import com.svym.inventory.service.entity.LocationStatistics;
import com.svym.inventory.service.entity.UserLocation;
import com.svym.inventory.service.repository.LocationStatisticsRepository;
import com.svym.inventory.service.location.UserLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationStatisticsService {

    private final LocationStatisticsRepository locationStatisticsRepository;
    private final UserLocationRepository userLocationRepository;

    public List<LocationStatisticsDto> getAllLocationStatistics() {
        List<LocationStatistics> statistics = locationStatisticsRepository.findAllActiveLocationStatistics();
        return statistics.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<LocationStatisticsDto> getAllLocationsWithStatistics(Long userId) {
        // Check if user has location mappings
        List<UserLocation> userLocations = userLocationRepository.findActiveLocationsByUserId(userId);

        List<LocationStatistics> statistics;

        if (userLocations.isEmpty()) {
            // No location mappings, return complete list
            statistics = locationStatisticsRepository.findAllActiveLocationStatistics();
        } else {
            // User has location mappings, filter by allowed locations
            List<Long> allowedLocationIds = userLocations.stream()
                    .map(UserLocation::getLocationId)
                    .collect(Collectors.toList());
            statistics = locationStatisticsRepository.findAllActiveLocationStatisticsByLocationIds(allowedLocationIds);
        }

        return statistics.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<LocationStatisticsDto> getLocationStatisticsById(Long locationId) {
        Optional<LocationStatistics> statistics = locationStatisticsRepository.findActiveByLocationId(locationId);
        return statistics.map(this::convertToDto);
    }

    public LocationStatisticsDto updateLocationStatistics(Long locationId, Integer stockStatusCount,
                                                         Integer expiredCount, Integer nearExpiryCount) {
        Optional<LocationStatistics> existingStats = locationStatisticsRepository.findByLocationIdAndIsActiveTrue(locationId);

        LocationStatistics statistics;
        if (existingStats.isPresent()) {
            statistics = existingStats.get();
            statistics.setStockStatusCount(stockStatusCount);
            statistics.setExpiredCount(expiredCount);
            statistics.setNearExpiryCount(nearExpiryCount);
        } else {
            statistics = new LocationStatistics();
            Location location = new Location();
            location.setId(locationId);
            statistics.setLocation(location);
            statistics.setStockStatusCount(stockStatusCount);
            statistics.setExpiredCount(expiredCount);
            statistics.setNearExpiryCount(nearExpiryCount);
        }

        LocationStatistics savedStats = locationStatisticsRepository.save(statistics);
        return convertToDto(savedStats);
    }

    private LocationStatisticsDto convertToDto(LocationStatistics statistics) {
        LocationStatisticsDto dto = new LocationStatisticsDto();
        dto.setId(statistics.getId());
        dto.setLocationId(statistics.getLocation().getId());
        dto.setLocationName(statistics.getLocation().getName());
        dto.setLocationAddress(statistics.getLocation().getLocationAddress());
        dto.setStockStatusCount(statistics.getStockStatusCount());
        dto.setExpiredCount(statistics.getExpiredCount());
        dto.setNearExpiryCount(statistics.getNearExpiryCount());
        dto.setLastUpdated(statistics.getLastUpdated());
        dto.setIsActive(statistics.getIsActive());
        return dto;
    }
}
