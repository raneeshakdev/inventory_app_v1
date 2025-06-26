package com.svym.inventory.service.medicinepbatch;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.svym.inventory.service.dto.MedicinePurchaseBatchDTO;
import com.svym.inventory.service.entity.MedicinePurchaseBatch;
import com.svym.inventory.service.purchasetype.PurchaseTypeRepository;
import com.svym.inventory.service.repository.MedicineRepository;
import com.svym.inventory.service.security.UserUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicinePurchaseBatchServiceImpl implements MedicinePurchaseBatchService {

    private final MedicinePurchaseBatchRepository repository;
    private final MedicineRepository medicineRepository; // Assuming you have a service to handle Medicine entity
    private final PurchaseTypeRepository purchaseTypeRepository; // Assuming you have a service to handle PurchaseType entity

    @Override
    public MedicinePurchaseBatchDTO create(MedicinePurchaseBatchDTO dto) {
        MedicinePurchaseBatch entity = mapToEntity(dto);
        return mapToDTO(repository.save(entity));
    }

    @Override
    public MedicinePurchaseBatchDTO getById(Long id) {
        MedicinePurchaseBatch entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Batch not found"));
        return mapToDTO(entity);
    }

    @Override
    public List<MedicinePurchaseBatchDTO> getAll() {
        return repository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public MedicinePurchaseBatchDTO update(Long id, MedicinePurchaseBatchDTO dto) {
        MedicinePurchaseBatch existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Batch not found"));
        existing.setBatchName(dto.getBatchName());
        existing.setCurrentQuantity(dto.getCurrentQuantity());
        existing.setExpiryDate(dto.getExpiryDate());
        existing.setInitialQuantity(dto.getInitialQuantity());
        existing.setTotalPrice(dto.getTotalPrice());
        existing.setUnitPrice(dto.getUnitPrice());
        existing.setIsActive(dto.getIsActive());
        existing.setLastModifiedBy(UserUtils.getCurrentUser());
        existing.setLastUpdatedAt(dto.getLastUpdatedAt());
     
        existing.setPurchaseType(purchaseTypeRepository.findById(dto.getPurchaseTypeId())
			.orElseThrow(() -> new EntityNotFoundException("Purchase Type not found")));
        existing.setMedicine(medicineRepository.findById(dto.getMedicineId())
			.orElseThrow(() -> new EntityNotFoundException("Medicine not found")));
        // Assume medicineId is handled elsewhere (or inject Medicine entity)
        return mapToDTO(repository.save(existing));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Batch not found");
        }
        repository.deleteById(id);
    }

    // Mapper methods
    private MedicinePurchaseBatchDTO mapToDTO(MedicinePurchaseBatch entity) {
        MedicinePurchaseBatchDTO dto = new MedicinePurchaseBatchDTO();
        dto.setId(entity.getId());
        dto.setBatchName(entity.getBatchName());
        dto.setCurrentQuantity(entity.getCurrentQuantity());
        dto.setExpiryDate(entity.getExpiryDate());
        dto.setInitialQuantity(entity.getInitialQuantity());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setUnitPrice(entity.getUnitPrice());
        dto.setPurchaseTypeId(entity.getPurchaseType().getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setMedicineId(entity.getMedicine().getId());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setLastUpdatedAt(entity.getLastUpdatedAt());
        dto.setLastModifiedBy(entity.getLastModifiedBy());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    private MedicinePurchaseBatch mapToEntity(MedicinePurchaseBatchDTO dto) {
        MedicinePurchaseBatch entity = new MedicinePurchaseBatch();
        entity.setId(dto.getId());
        entity.setBatchName(dto.getBatchName());
        entity.setCurrentQuantity(dto.getCurrentQuantity());
        entity.setExpiryDate(dto.getExpiryDate());
        entity.setInitialQuantity(dto.getInitialQuantity());
        entity.setTotalPrice(dto.getTotalPrice());
        entity.setUnitPrice(dto.getUnitPrice());
        entity.setIsActive(dto.getIsActive());
        entity.setCreatedBy(UserUtils.getCurrentUser()); // Assuming you have a UserContext to get the current user
        entity.setLastModifiedBy(UserUtils.getCurrentUser());
        if(dto.getPurchaseTypeId() != null) {
			entity.setPurchaseType(purchaseTypeRepository.findById(dto.getPurchaseTypeId())
				.orElseThrow(() -> new EntityNotFoundException("Purchase Type not found")));
		}
        if(dto.getMedicineId() != null) {
			entity.setMedicine(medicineRepository.findById(dto.getMedicineId())
				.orElseThrow(() -> new EntityNotFoundException("Medicine not found")));
		}
        return entity;
    }
}
