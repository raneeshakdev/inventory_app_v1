package com.svym.inventory.service.userlocation;

import com.svym.inventory.service.dto.UserLocationDTO;
import com.svym.inventory.service.dto.UserLocationUpdateRequest;
import com.svym.inventory.service.entity.UserLocation;
import com.svym.inventory.service.location.LocationRepository;
import com.svym.inventory.service.location.UserLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserLocationServiceImpl implements UserLocationService {

    private final UserLocationRepository userLocationRepository;
    private final LocationRepository locationRepository;

    @Override
    public List<UserLocationDTO> getUserLocationMappings(Long userId) {
        List<UserLocation> userLocations = userLocationRepository.findActiveLocationsByUserId(userId);

        return userLocations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<UserLocationDTO> updateUserLocationMappings(UserLocationUpdateRequest request) {
        Long userId = request.getUserId();
        List<Long> newLocationIds = request.getLocationIds();

        // Get existing active mappings
        List<UserLocation> existingMappings = userLocationRepository.findActiveLocationsByUserId(userId);

        // Get existing location IDs
        List<Long> existingLocationIds = existingMappings.stream()
                .map(UserLocation::getLocationId)
                .collect(Collectors.toList());

        // Find mappings to deactivate (existing but not in new list)
        existingMappings.stream()
                .filter(mapping -> !newLocationIds.contains(mapping.getLocationId()))
                .forEach(mapping -> {
                    mapping.setIsActive(false);
                    userLocationRepository.save(mapping);
                });

        // Find mappings to add (in new list but not existing)
        newLocationIds.stream()
                .filter(locationId -> !existingLocationIds.contains(locationId))
                .forEach(locationId -> {
                    UserLocation newMapping = new UserLocation();
                    newMapping.setUserId(userId);
                    newMapping.setLocationId(locationId);
                    newMapping.setIsActive(true);
                    userLocationRepository.save(newMapping);
                });

        // Reactivate any previously deactivated mappings that are now in the new list
        List<UserLocation> allUserMappings = userLocationRepository.findAll().stream()
                .filter(ul -> ul.getUserId().equals(userId))
                .collect(Collectors.toList());

        allUserMappings.stream()
                .filter(mapping -> !mapping.getIsActive() && newLocationIds.contains(mapping.getLocationId()))
                .forEach(mapping -> {
                    mapping.setIsActive(true);
                    userLocationRepository.save(mapping);
                });

        // Return updated mappings
        return getUserLocationMappings(userId);
    }

    private UserLocationDTO convertToDTO(UserLocation userLocation) {
        UserLocationDTO dto = new UserLocationDTO();
        dto.setId(userLocation.getId());
        dto.setUserId(userLocation.getUserId());
        dto.setLocationId(userLocation.getLocationId());
        dto.setIsActive(userLocation.getIsActive());
        dto.setCreatedAt(userLocation.getCreatedAt());
        dto.setUpdatedAt(userLocation.getUpdatedAt());

        // Optionally populate location details
        locationRepository.findById(userLocation.getLocationId()).ifPresent(location -> {
            dto.setLocationName(location.getName());
            dto.setLocationAddress(location.getLocationAddress());
        });

        return dto;
    }
}

