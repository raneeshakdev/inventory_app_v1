package com.svym.inventory.service.view.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.svym.inventory.service.view.MedicineListDTO;
import com.svym.inventory.service.view.service.MedicineListService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/medicine-list")
@RequiredArgsConstructor
public class MedicineListController {

    private final MedicineListService medicineListService;

    /**
     * Get medicines by location ID
     * @param locationId The location ID to filter medicines
     * @return List of medicines for the specified location
     */
    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<MedicineListDTO>> getMedicinesByLocationId(@PathVariable Long locationId) {
        List<MedicineListDTO> medicines = medicineListService.getMedicinesByLocationId(locationId);
        return ResponseEntity.ok(medicines);
    }

    /**
     * Get medicines with optional location filter
     * If locationId is provided, returns medicines for that location
     * If locationId is not provided, returns all medicines
     * @param locationId Optional location ID parameter
     * @return List of medicines
     */
    @GetMapping
    public ResponseEntity<List<MedicineListDTO>> getMedicines(@RequestParam(required = false) Long locationId) {
        List<MedicineListDTO> medicines;
        if (locationId != null) {
            medicines = medicineListService.getMedicinesByLocationId(locationId);
        } else {
            medicines = medicineListService.getAllMedicines();
        }
        return ResponseEntity.ok(medicines);
    }
}
