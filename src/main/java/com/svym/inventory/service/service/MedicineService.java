package com.svym.inventory.service.service;

import com.svym.inventory.service.dto.MedicineDto;
import com.svym.inventory.service.entity.Medicine;

import java.util.List;

public interface MedicineService {
    Medicine save(Medicine medicine);
    Medicine update(Long id, Medicine medicine);
    Medicine findById(Long id);

    MedicineDto findDtoById(Long id);

    List<Medicine> findAll();
    void delete(Long id);
    List<Medicine> findOutOfStockMedicines();
    List<Medicine> findLowStockMedicines();
    List<Medicine> findAvailableMedicines();

    List<MedicineDto> findAllMedicineAsDtoList();

    MedicineDto saveDto(MedicineDto medicineDto);

    MedicineDto createMedicineWithLocationStock(MedicineDto medicineDto);
}
