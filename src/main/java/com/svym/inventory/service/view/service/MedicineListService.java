package com.svym.inventory.service.view.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.svym.inventory.service.view.MedicineListDTO;
import com.svym.inventory.service.view.MedicineListProjection;
import com.svym.inventory.service.view.repository.MedicineListRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicineListService {

    private final MedicineListRepository repository;

    public List<MedicineListDTO> getMedicinesByLocationId(Long locationId) {
        List<MedicineListProjection> projections = repository.findMedicinesByLocationId(locationId);
        return mapProjectionsToDTO(projections);
    }

    public List<MedicineListDTO> getAllMedicines() {
        List<MedicineListProjection> projections = repository.findAllMedicines();
        return mapProjectionsToDTO(projections);
    }

    private List<MedicineListDTO> mapProjectionsToDTO(List<MedicineListProjection> projections) {
        return projections.stream().map(p -> {
            MedicineListDTO dto = new MedicineListDTO();
            dto.setMedicineId(p.getMedicineId());
            dto.setMedicineName(p.getMedicineName());
            dto.setMedicineTypeName(p.getMedicineTypeName());
            dto.setLocationId(p.getLocationId());
            dto.setLocationName(p.getLocationName());
            dto.setNumberOfBatches(p.getNumberOfBatches());
            dto.setStockStatus(p.getStockStatus());
            dto.setHasExpiredBatches(p.getHasExpiredBatches());
            dto.setTotalNumberOfMedicines(p.getTotalNumberOfMedicines());
            dto.setNumberOfMedExpired(p.getNumberOfMedExpired());
            return dto;
        }).collect(Collectors.toList());
    }
}
