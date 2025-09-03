package com.svym.inventory.service.view.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.svym.inventory.service.payload.response.ApiResponse;
import com.svym.inventory.service.view.MedicineLocationDTO;
import com.svym.inventory.service.view.service.MedicineLocationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/medicine-locations")
@RequiredArgsConstructor
public class MedicineLocationController {

    private final MedicineLocationService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicineLocationDTO>>> getAllMedicineLocations() {
        try {
            List<MedicineLocationDTO> medicines = service.getAllMedicineLocations();
            return ResponseEntity.ok(ApiResponse.success(medicines, "All medicine locations retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve medicine locations: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MedicineLocationDTO>>> searchAvailableMedicines(
            @RequestParam String search) {
        try {
            if (search == null || search.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Search term is required and cannot be empty"));
            }

            List<MedicineLocationDTO> medicines = service.searchAvailableMedicinesByName(search.trim());
            return ResponseEntity.ok(ApiResponse.success(medicines,
                    "Available medicines matching '" + search + "' retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to search medicines: " + e.getMessage()));
        }
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<ApiResponse<List<MedicineLocationDTO>>> getMedicinesByLocationId(
            @PathVariable Long locationId,
            @RequestParam(required = false) String search) {
        try {
            if (locationId == null || locationId <= 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid location ID. Location ID must be a positive number"));
            }

            List<MedicineLocationDTO> medicines;
            String message;

            if (search != null && !search.trim().isEmpty()) {
                medicines = service.searchMedicinesByLocationIdAndName(locationId, search.trim());
                message = "Medicines matching '" + search + "' for location " + locationId + " retrieved successfully";
            } else {
                medicines = service.getMedicineLocationsByLocationId(locationId);
                message = "Medicines for location " + locationId + " retrieved successfully";
            }

            return ResponseEntity.ok(ApiResponse.success(medicines, message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve medicines for location " + locationId + ": " + e.getMessage()));
        }
    }

    @GetMapping("/location/{locationId}/available")
    public ResponseEntity<ApiResponse<List<MedicineLocationDTO>>> getAvailableMedicinesByLocationId(
            @PathVariable Long locationId,
            @RequestParam(required = false) String search) {
        try {
            if (locationId == null || locationId <= 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid location ID. Location ID must be a positive number"));
            }

            List<MedicineLocationDTO> medicines;
            String message;

            if (search != null && !search.trim().isEmpty()) {
                medicines = service.searchAvailableMedicinesByLocationIdAndName(locationId, search.trim());
                message = "Available medicines matching '" + search + "' for location " + locationId + " retrieved successfully";
            } else {
                medicines = service.getAvailableMedicinesByLocationId(locationId);
                message = "Available medicines for location " + locationId + " retrieved successfully";
            }

            return ResponseEntity.ok(ApiResponse.success(medicines, message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve available medicines for location " + locationId + ": " + e.getMessage()));
        }
    }
}
