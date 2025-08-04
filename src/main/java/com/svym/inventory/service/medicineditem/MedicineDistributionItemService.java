package com.svym.inventory.service.medicineditem;

import java.util.List;

import com.svym.inventory.service.dto.MedicineDistributionItemDTO;

public interface MedicineDistributionItemService {
    MedicineDistributionItemDTO create(MedicineDistributionItemDTO dto);
    MedicineDistributionItemDTO getById(Long id);
    List<MedicineDistributionItemDTO> getAll();
    MedicineDistributionItemDTO update(Long id, MedicineDistributionItemDTO dto);
    void delete(Long id);
}
