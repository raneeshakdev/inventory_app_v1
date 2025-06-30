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
		if (entity.getMedicine() != null) {
			dto.setMedicineId(entity.getMedicine().getId());
		}
		if (entity.getDistribution() != null) {
			dto.setDistributionId(entity.getDistribution().getId());
		}
		if (entity.getBatch() != null) {
			dto.setBatchId(entity.getBatch().getId());
		}
		dto.setUnitPrice(entity.getUnitPrice());
		dto.setTotalPrice(entity.getTotalPrice());
		return dto;
	}

	private MedicineDistributionItem convertToEntity(MedicineDistributionItemDTO dto) {
		MedicineDistributionItem entity = new MedicineDistributionItem();
		entity.setId(dto.getId());
		entity.setQuantity(dto.getQuantity());
		if (dto.getMedicineId() != null) {
			entity.setMedicine(medicineRepository.findById(dto.getMedicineId())
					.orElseThrow(() -> new EntityNotFoundException("Medicine not found")));
		}
		if (dto.getDistributionId() != null) {
			entity.setDistribution(distributionRepository.findById(dto.getDistributionId())
					.orElseThrow(() -> new EntityNotFoundException("Distribution not found")));
		}
		if (dto.getBatchId() != null) {
			entity.setBatch(medicinePurchaseBatchRepository.findById(dto.getBatchId())
					.orElseThrow(() -> new EntityNotFoundException("Batch not found")));
		}
		if (dto.getUnitPrice() != null) {
			entity.setUnitPrice(dto.getUnitPrice());
		} else {
			entity.setUnitPrice(0.0); // Default value if not provided
		}
		if (dto.getTotalPrice() != null) {
			entity.setTotalPrice(dto.getTotalPrice());
		} else {
			entity.setTotalPrice(0.0); // Default value if not provided
		}
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
		if (dto.getMedicineId() != null) {
			existing.setMedicine(medicineRepository.findById(dto.getMedicineId())
					.orElseThrow(() -> new EntityNotFoundException("Medicine not found")));
		}
		if (dto.getDistributionId() != null) {
			existing.setDistribution(distributionRepository.findById(dto.getDistributionId())
					.orElseThrow(() -> new EntityNotFoundException("Distribution not found")));
		}
		if (dto.getBatchId() != null) {
			existing.setBatch(medicinePurchaseBatchRepository.findById(dto.getBatchId())
					.orElseThrow(() -> new EntityNotFoundException("Batch not found")));
		}
		if (dto.getUnitPrice() != null) {
			existing.setUnitPrice(dto.getUnitPrice());
		} else {
			existing.setUnitPrice(0.0); // Default value if not provided
		}
		if (dto.getTotalPrice() != null) {
			existing.setTotalPrice(dto.getTotalPrice());
		} else {
			existing.setTotalPrice(0.0); // Default value if not provided
		}
		// Save the updated entity and convert it to DTO
		return convertToDTO(repository.save(existing));
	}

	@Override
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new EntityNotFoundException("Item not found");
		}
		repository.deleteById(id);
	}
}
