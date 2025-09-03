package com.svym.inventory.service.view.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.svym.inventory.service.view.MedicineLocationDTO;
import com.svym.inventory.service.view.MedicineLocationProjection;
import com.svym.inventory.service.view.repository.MedicineLocationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicineLocationService {

    private final MedicineLocationRepository repository;

    public List<MedicineLocationDTO> getAllMedicineLocations() {
        List<MedicineLocationProjection> projections = repository.findAllMedicineLocations();
        return projections.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineLocationDTO> getMedicineLocationsByLocationId(Long locationId) {
        List<MedicineLocationProjection> projections = repository.findByLocationId(locationId);
        return projections.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineLocationDTO> getAvailableMedicinesByLocationId(Long locationId) {
        List<MedicineLocationProjection> projections = repository.findByLocationIdWithAvailableMedicines(locationId);
        return projections.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineLocationDTO> searchAvailableMedicinesByLocationIdAndName(Long locationId, String searchTerm) {
        List<MedicineLocationProjection> projections = repository.searchAvailableMedicinesByLocationIdAndName(locationId, searchTerm);
        return projections.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineLocationDTO> searchMedicinesByLocationIdAndName(Long locationId, String searchTerm) {
        List<MedicineLocationProjection> projections = repository.searchMedicinesByLocationIdAndName(locationId, searchTerm);
        return projections.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineLocationDTO> searchAvailableMedicinesByName(String searchTerm) {
        List<MedicineLocationProjection> projections = repository.searchAvailableMedicinesByName(searchTerm);
        return projections.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MedicineLocationDTO convertToDTO(MedicineLocationProjection projection) {
        MedicineLocationDTO dto = new MedicineLocationDTO();
        dto.setMedicineName(projection.getMedicineName());
        dto.setNumberOfBatches(projection.getNumberOfBatches());
        dto.setLocationId(projection.getLocationId());
        dto.setMedicineId(projection.getMedicineId());
        dto.setTotalNumberOfMedicines(projection.getTotalNumberOfMedicines());
        return dto;
    }
}
