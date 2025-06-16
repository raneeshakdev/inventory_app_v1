package com.svym.inventory.service.entity.mapper;

import com.svym.inventory.service.dto.MedicineDto;
import com.svym.inventory.service.entity.Medicine;
import org.springframework.stereotype.Component;

@Component
public class MedicineMapper {
    public MedicineDto toDto(Medicine medicine) {
        if (medicine == null) return null;

        MedicineDto vo = new MedicineDto();
        vo.setId(medicine.getId());
        vo.setName(medicine.getName());
        vo.setTypeId(medicine.getType().getId());
        vo.setTypeName(medicine.getType().getTypeName());
        vo.setCreatedAt(medicine.getCreatedAt());
        vo.setCreatedBy(medicine.getCreatedBy());
        vo.setLastModifiedAt(medicine.getLastModifiedAt());
        vo.setLastModifiedBy(medicine.getLastModifiedBy());
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
        medicine.setName(vo.getName());
        medicine.setCurrentBatchesCount(vo.getCurrentBatchesCount());
        medicine.setStockThreshold(vo.getStockThreshold());
        medicine.setOutOfStock(vo.getOutOfStock());
        medicine.setIsActive(vo.getIsActive());
        medicine.setLastModifiedBy(vo.getLastModifiedBy());
        return medicine;
    }
}
