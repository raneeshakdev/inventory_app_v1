package com.svym.inventory.service.view.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.svym.inventory.service.view.ExpiringMedicineDTO;
import com.svym.inventory.service.view.ExpiringMedicineProjection;
import com.svym.inventory.service.view.repository.ExpiringMedicineRepository;

@Service
public class ExpiringMedicineService {

    private final ExpiringMedicineRepository repository;

    public ExpiringMedicineService(ExpiringMedicineRepository repository) {
        this.repository = repository;
    }

    public List<ExpiringMedicineDTO> getExpiringMedicines() {
        List<ExpiringMedicineProjection> projections = repository.findExpiringMedicines();
        return projections.stream().map(p -> {
            ExpiringMedicineDTO dto = new ExpiringMedicineDTO();
            dto.setMedicineName(p.getMedicineName());
            dto.setBatchName(p.getBatchName());
            dto.setCurrentQuantity(p.getCurrentQuantity());
            dto.setExpiryDate(p.getExpiryDate());
            dto.setDaysToExpiry(p.getDaysToExpiry());
            return dto;
        }).collect(Collectors.toList());
    }
}
