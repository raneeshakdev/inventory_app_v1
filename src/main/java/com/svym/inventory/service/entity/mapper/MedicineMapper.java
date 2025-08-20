package com.svym.inventory.service.entity.mapper;

import com.svym.inventory.service.dto.MedicineDto;
import com.svym.inventory.service.entity.Medicine;
import com.svym.inventory.service.entity.MedicineType;
import com.svym.inventory.service.medicinetype.MedicineTypeRepository;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.EntityNotFoundException;

@Component
@RequiredArgsConstructor
public class MedicineMapper {
    private final MedicineTypeRepository medicineTypeRepository;

    public MedicineDto toDto(Medicine medicine) {
        if (medicine == null) return null;

        MedicineDto vo = new MedicineDto();
        vo.setId(medicine.getId());
        vo.setMedicineName(medicine.getMedicineName());
        vo.setTypeId(medicine.getType().getId());
        vo.setTypeName(medicine.getType().getTypeName());
        vo.setCreatedAt(medicine.getCreatedAt());
        vo.setLastModifiedAt(medicine.getLastModifiedAt());
        vo.setCurrentBatchesCount(medicine.getCurrentBatchesCount());
        vo.setStockThreshold(medicine.getStockThreshold());
        vo.setOutOfStock(medicine.getOutOfStock());
        vo.setIsActive(medicine.getIsActive());
        return vo;
    }

    public Medicine toEntity(MedicineDto vo) {
        if (vo == null) return null;

        Medicine medicine = new Medicine();
        medicine.setId(vo.getId());
        medicine.setMedicineName(vo.getMedicineName());

        // Handle MedicineType relationship
        if (vo.getTypeId() != null) {
            MedicineType medicineType = medicineTypeRepository.findById(vo.getTypeId())
                .orElseThrow(() -> new EntityNotFoundException("MedicineType not found with id: " + vo.getTypeId()));
            medicine.setType(medicineType);
        }

        medicine.setCurrentBatchesCount(vo.getCurrentBatchesCount() != null ? vo.getCurrentBatchesCount() : 0);
        medicine.setStockThreshold(vo.getStockThreshold());
        medicine.setOutOfStock(vo.getOutOfStock() != null ? vo.getOutOfStock() : false);
        medicine.setIsActive(vo.getIsActive() != null ? vo.getIsActive() : true);
        return medicine;
    }
}
