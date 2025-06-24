package com.svym.inventory.service.medicinetype;

import java.util.List;

import com.svym.inventory.service.dto.MedicineTypeDTO;

public interface MedicineTypeService {
    MedicineTypeDTO create(MedicineTypeDTO dto);
    MedicineTypeDTO getById(Long id);
    List<MedicineTypeDTO> getAll();
    MedicineTypeDTO update(Long id, MedicineTypeDTO dto);
    void delete(Long id);
}
