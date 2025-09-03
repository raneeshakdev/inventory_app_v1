package com.svym.inventory.service.medicineditem;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.svym.inventory.service.dto.MedicineDistributionItemDTO;
import com.svym.inventory.service.entity.MedicineDistributionItem;
import com.svym.inventory.service.medicinedistribution.MedicineDistributionRepository;
import com.svym.inventory.service.medicinepbatch.MedicinePurchaseBatchRepository;
import com.svym.inventory.service.repository.MedicineRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicineDistributionItemServiceImpl implements MedicineDistributionItemService {

	private final MedicineDistributionItemRepository repository;
	private final MedicineDistributionRepository distributionRepository;
	private final MedicineRepository medicineRepository;
	private final MedicinePurchaseBatchRepository medicinePurchaseBatchRepository;

	private MedicineDistributionItemDTO convertToDTO(MedicineDistributionItem entity) {
		MedicineDistributionItemDTO dto = new MedicineDistributionItemDTO();
		dto.setId(entity.getId());
		dto.setQuantity(entity.getQuantity());
		dto.setUnitPrice(entity.getUnitPrice());
		dto.setTotalPrice(entity.getTotalPrice());

		// Set medicine info using nested DTO
		if (entity.getMedicine() != null) {
			MedicineDistributionItemDTO.MedicineDTO medicineDTO = new MedicineDistributionItemDTO.MedicineDTO();
			medicineDTO.setId(entity.getMedicine().getId());
			dto.setMedicine(medicineDTO);
		}

		// Set batch info using nested DTO
		if (entity.getBatch() != null) {
			MedicineDistributionItemDTO.BatchDTO batchDTO = new MedicineDistributionItemDTO.BatchDTO();
			batchDTO.setId(entity.getBatch().getId());
			dto.setBatch(batchDTO);
		}

		return dto;
	}

	private MedicineDistributionItem convertToEntity(MedicineDistributionItemDTO dto) {
		MedicineDistributionItem entity = new MedicineDistributionItem();
		entity.setId(dto.getId());
		entity.setQuantity(dto.getQuantity());

		// Handle medicine using nested DTO
		if (dto.getMedicine() != null && dto.getMedicine().getId() != null) {
			entity.setMedicine(medicineRepository.findById(dto.getMedicine().getId())
					.orElseThrow(() -> new EntityNotFoundException("Medicine not found")));
		}

		// Handle batch using nested DTO
		if (dto.getBatch() != null && dto.getBatch().getId() != null) {
			entity.setBatch(medicinePurchaseBatchRepository.findById(dto.getBatch().getId())
					.orElseThrow(() -> new EntityNotFoundException("Batch not found")));
		}

		entity.setUnitPrice(dto.getUnitPrice() != null ? dto.getUnitPrice() : 0.0);
		entity.setTotalPrice(dto.getTotalPrice() != null ? dto.getTotalPrice() : 0.0);

		return entity;
	}

	@Override
	public MedicineDistributionItemDTO create(MedicineDistributionItemDTO dto) {
		MedicineDistributionItem saved = repository.save(convertToEntity(dto));
		return convertToDTO(saved);
	}

	@Override
	public MedicineDistributionItemDTO getById(Long id) {
		return repository.findById(id).map(this::convertToDTO)
				.orElseThrow(() -> new EntityNotFoundException("Item not found"));
	}

	@Override
	public List<MedicineDistributionItemDTO> getAll() {
		return repository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public MedicineDistributionItemDTO update(Long id, MedicineDistributionItemDTO dto) {
		MedicineDistributionItem existing = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Item not found"));

		existing.setQuantity(dto.getQuantity());

		// Handle medicine using nested DTO
		if (dto.getMedicine() != null && dto.getMedicine().getId() != null) {
			existing.setMedicine(medicineRepository.findById(dto.getMedicine().getId())
					.orElseThrow(() -> new EntityNotFoundException("Medicine not found")));
		}

		// Handle batch using nested DTO
		if (dto.getBatch() != null && dto.getBatch().getId() != null) {
			existing.setBatch(medicinePurchaseBatchRepository.findById(dto.getBatch().getId())
					.orElseThrow(() -> new EntityNotFoundException("Batch not found")));
		}

		existing.setUnitPrice(dto.getUnitPrice() != null ? dto.getUnitPrice() : existing.getUnitPrice());
		existing.setTotalPrice(dto.getTotalPrice() != null ? dto.getTotalPrice() : existing.getTotalPrice());

		return convertToDTO(repository.save(existing));
	}

	@Override
	public void delete(Long id) {
		MedicineDistributionItem entity = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Item not found"));
		repository.delete(entity);
	}
}
