package com.svym.inventory.service.service;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.svym.inventory.service.dto.LocationDTO;
import com.svym.inventory.service.dto.MedicineDto;
import com.svym.inventory.service.entity.Medicine;
import com.svym.inventory.service.entity.MedicineLocationStock;
import com.svym.inventory.service.entity.mapper.MedicineMapper;
import com.svym.inventory.service.location.LocationService;
import com.svym.inventory.service.repository.MedicineLocationStockRepository;
import com.svym.inventory.service.repository.MedicineRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService{
    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;
    private final MedicineLocationStockRepository medicineLocationStockRepository;
    private final LocationService locationService;

    @Override
    @Transactional
    public Medicine save(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    @Override
    @Transactional
    public Medicine update(Long id, Medicine medicine) {
        Medicine existingMedicine = findById(id);
        existingMedicine.setMedicineName(medicine.getMedicineName());
        existingMedicine.setType(medicine.getType());
        existingMedicine.setCurrentBatchesCount(medicine.getCurrentBatchesCount());
        existingMedicine.setStockThreshold(medicine.getStockThreshold());
        existingMedicine.setOutOfStock(medicine.getOutOfStock());
        existingMedicine.setIsActive(medicine.getIsActive());
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
        Medicine savedMedicine = save(medicine);
        return medicineMapper.toDto(savedMedicine);
    }

    @Override
    @Transactional
    public MedicineDto createMedicineWithLocationStock(MedicineDto medicineDto) {
        // Ensure ID is null for new medicine creation
        medicineDto.setId(null);

        // Save the medicine first
        Medicine medicine = medicineMapper.toEntity(medicineDto);
        Medicine savedMedicine = save(medicine);

        // Get all available locations
        List<LocationDTO> locations = locationService.getAll();

        // Create medicine location stock entries for each location with default values
        for (LocationDTO location : locations) {
            MedicineLocationStock medicineLocationStock = new MedicineLocationStock();
            medicineLocationStock.setMedicineId(savedMedicine.getId());
            medicineLocationStock.setLocationId(location.getId());
            medicineLocationStock.setNumberOfBatches((short) 0);
            medicineLocationStock.setIsOutOfStock(true); // Default to out of stock since no batches initially
            medicineLocationStock.setHasExpiredBatches(false);
            medicineLocationStock.setTotalNumberOfMedicines(0);
            medicineLocationStock.setNumberOfMedExpired(0);

            medicineLocationStockRepository.save(medicineLocationStock);
        }

        return medicineMapper.toDto(savedMedicine);
    }

}
