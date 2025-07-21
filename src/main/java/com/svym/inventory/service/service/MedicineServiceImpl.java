package com.svym.inventory.service.service;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.svym.inventory.service.dto.MedicineDto;
import com.svym.inventory.service.entity.Medicine;
import com.svym.inventory.service.entity.mapper.MedicineMapper;
import com.svym.inventory.service.repository.MedicineRepository;
import com.svym.inventory.service.security.UserUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService{
    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;

    @Override
    @Transactional
    public Medicine save(Medicine medicine) {
    	medicine.setCreatedBy(UserUtils.getCurrentUser().getId().toString());
        return medicineRepository.save(medicine);
    }

    @Override
    @Transactional
    public Medicine update(Long id, Medicine medicine) {
        Medicine existingMedicine = findById(id);
        existingMedicine.setName(medicine.getName());
        existingMedicine.setType(medicine.getType());
        existingMedicine.setCurrentBatchesCount(medicine.getCurrentBatchesCount());
        existingMedicine.setStockThreshold(medicine.getStockThreshold());
        existingMedicine.setOutOfStock(medicine.getOutOfStock());
        existingMedicine.setIsActive(medicine.getIsActive());
        existingMedicine.setLastModifiedBy(UserUtils.getCurrentUser().getId().toString());
        return medicineRepository.save(existingMedicine);
    }

    @Override
    public Medicine findById(Long id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medicine not found with id: " + id));
    }

    @Override
    public List<Medicine> findAll() {
        return StreamSupport.stream(medicineRepository.findAll().spliterator(), false)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Medicine medicine = findById(id);
        medicine.setIsActive(false);
        medicineRepository.save(medicine);
    }

    @Override
    public List<Medicine> findOutOfStockMedicines() {
        return List.of();
    }

    @Override
    public List<Medicine> findLowStockMedicines() {
        return List.of();
    }


    @Override
    public List<Medicine> findAvailableMedicines() {
        return medicineRepository.findAvailableMedicines();
    }

    @Override
    public MedicineDto findDtoById(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medicine not found with id: " + id));
        return medicineMapper.toDto(medicine);
    }

    @Override
    public List<MedicineDto> findAllMedicineAsDtoList() {
        return findAll().stream()
                .map(medicineMapper::toDto)
                .toList();
    }

    @Override
    public MedicineDto saveDto(MedicineDto medicineDto) {
        Medicine medicine = medicineMapper.toEntity(medicineDto);
        medicine.setCreatedBy(UserUtils.getCurrentUser().getId().toString());
        Medicine savedMedicine = save(medicine);
        return medicineMapper.toDto(savedMedicine);
    }

}
