package com.svym.inventory.service.controller;

import com.svym.inventory.service.dto.MedicineActionItemsDetailsDTO;
import com.svym.inventory.service.service.MedicineActionItemsDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing medicine action items details.
 * Provides endpoints for accessing the medicine_action_items_details view.
 */
@RestController
@RequestMapping("/api/v1/medicine-action-items-details")
@RequiredArgsConstructor
@Slf4j
public class MedicineActionItemsDetailsController {

    private final MedicineActionItemsDetailsService medicineActionItemsDetailsService;

    /**
     * Get all action items for a specific location.
     * @param locationId the location ID to filter by
     * @return ResponseEntity containing list of medicine action items details for the location
     */
    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<MedicineActionItemsDetailsDTO>> getAllActionItemsByLocation(
            @PathVariable Long locationId) {
        log.info("Fetching all action items for location ID: {}", locationId);

        List<MedicineActionItemsDetailsDTO> actionItems =
                medicineActionItemsDetailsService.getAllActionItemsByLocation(locationId);

        log.info("Retrieved {} action items for location ID: {}", actionItems.size(), locationId);

        return ResponseEntity.ok(actionItems);
    }
}
