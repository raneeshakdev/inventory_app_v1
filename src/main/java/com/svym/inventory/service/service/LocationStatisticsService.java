package com.svym.inventory.service.service;

import com.svym.inventory.service.dto.LocationStatisticsDto;
import com.svym.inventory.service.entity.Location;
import com.svym.inventory.service.entity.LocationStatistics;
import com.svym.inventory.service.repository.LocationStatisticsRepository;
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

    public List<LocationStatisticsDto> getAllLocationStatistics() {
        List<LocationStatistics> statistics = locationStatisticsRepository.findAllActiveLocationStatistics();
        return statistics.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<LocationStatisticsDto> getAllLocationsWithStatistics() {
        List<LocationStatistics> statistics = locationStatisticsRepository.findAllActiveLocationStatistics();
        return statistics.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<LocationStatisticsDto> getLocationStatisticsById(Long locationId) {
        Optional<LocationStatistics> statistics = locationStatisticsRepository.findActiveByLocationId(locationId);
        return statistics.map(this::convertToDto);
    }

    public LocationStatisticsDto updateLocationStatistics(Long locationId, Integer outOfStockCount,
                                                         Integer expiredCount, Integer nearExpiryCount) {
        Optional<LocationStatistics> existingStats = locationStatisticsRepository.findByLocationIdAndIsActiveTrue(locationId);

        LocationStatistics statistics;
        if (existingStats.isPresent()) {
            statistics = existingStats.get();
            statistics.setOutOfStockCount(outOfStockCount);
            statistics.setExpiredCount(expiredCount);
            statistics.setNearExpiryCount(nearExpiryCount);
        } else {
            statistics = new LocationStatistics();
            Location location = new Location();
            location.setId(locationId);
            statistics.setLocation(location);
            statistics.setOutOfStockCount(outOfStockCount);
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
        dto.setOutOfStockCount(statistics.getOutOfStockCount());
        dto.setExpiredCount(statistics.getExpiredCount());
        dto.setNearExpiryCount(statistics.getNearExpiryCount());
        dto.setLastUpdated(statistics.getLastUpdated());
        dto.setIsActive(statistics.getIsActive());
        return dto;
    }
}
