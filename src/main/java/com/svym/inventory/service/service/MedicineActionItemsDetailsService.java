package com.svym.inventory.service.service;

import com.svym.inventory.service.dto.MedicineActionItemsDetailsDTO;
import com.svym.inventory.service.entity.MedicineActionItemsDetails;
import com.svym.inventory.service.repository.MedicineActionItemsDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing medicine action items details.
 * Provides business logic for accessing the medicine_action_items_details view.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MedicineActionItemsDetailsService {

    private final MedicineActionItemsDetailsRepository repository;
    private final ModelMapper modelMapper;

    /**
     * Get all action items for a specific location.
     * @param locationId the location ID to filter by
     * @return list of medicine action items details DTOs for the location
     */
    public List<MedicineActionItemsDetailsDTO> getAllActionItemsByLocation(Long locationId) {
        log.info("Fetching all action items for location ID: {}", locationId);

        List<MedicineActionItemsDetails> actionItems = repository.findAllByLocationId(locationId);

        log.info("Found {} action items for location ID: {}", actionItems.size(), locationId);

        return actionItems.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert entity to DTO.
     * @param entity the entity to convert
     * @return the converted DTO
     */
    private MedicineActionItemsDetailsDTO convertToDTO(MedicineActionItemsDetails entity) {
        return modelMapper.map(entity, MedicineActionItemsDetailsDTO.class);
    }
}
