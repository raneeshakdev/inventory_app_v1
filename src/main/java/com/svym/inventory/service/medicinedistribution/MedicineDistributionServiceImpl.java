package com.svym.inventory.service.medicinedistribution;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.svym.inventory.service.deliverycenter.DeliveryCenterRepository;
import com.svym.inventory.service.dto.MedicineDistributionDTO;
import com.svym.inventory.service.dto.MedicineDistributionItemDTO;
import com.svym.inventory.service.entity.MedicineDistribution;
import com.svym.inventory.service.entity.MedicineDistributionItem;
import com.svym.inventory.service.entity.MedicinePurchaseBatch;
import com.svym.inventory.service.entity.mapper.MedicineDistributionMapper;
import com.svym.inventory.service.medicineditem.MedicineDistributionItemRepository;
import com.svym.inventory.service.medicinepbatch.MedicinePurchaseBatchRepository;
import com.svym.inventory.service.patientdetail.PatientDetailRepository;
import com.svym.inventory.service.repository.MedicineRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicineDistributionServiceImpl implements MedicineDistributionService {

	private final MedicineDistributionRepository repository;
	private final MedicineDistributionMapper mapper;
	private final PatientDetailRepository patientDetailRepository;
	private final DeliveryCenterRepository deliveryCenterRepository;
	private final MedicineDistributionItemRepository distributionItemRepository;
	private final MedicinePurchaseBatchRepository medicinePurchaseBatchRepository;
	private final MedicineRepository medicineRepository;

	@Override
	@Transactional
	public MedicineDistributionDTO create(MedicineDistributionDTO dto) {
		// Check if distribution already exists for patient, delivery center and date
		Optional<MedicineDistribution> existingDistribution = repository
				.findByPatientIdAndDeliveryCenterIdAndDistributionDate(
						dto.getPatientId(),
						dto.getDeliveryCenterId(),
						dto.getDistributionDate());

		MedicineDistribution distribution;

		if (existingDistribution.isPresent()) {
			// Use existing distribution
			distribution = existingDistribution.get();
		} else {
			// Create new distribution
			distribution = new MedicineDistribution();
			distribution.setPatient(patientDetailRepository.findById(dto.getPatientId())
					.orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + dto.getPatientId())));
			distribution.setDeliveryCenter(deliveryCenterRepository.findById(dto.getDeliveryCenterId())
					.orElseThrow(() -> new EntityNotFoundException("DeliveryCenter not found with ID: " + dto.getDeliveryCenterId())));
			distribution.setDistributionDate(dto.getDistributionDate());
			distribution.setCreatedBy(dto.getCreatedBy());
			distribution = repository.save(distribution);
		}

		// Process distribution items
		for (MedicineDistributionItemDTO itemDTO : dto.getDistributionItems()) {
			processDistributionItem(distribution, itemDTO);
		}

		return mapper.toDTO(repository.save(distribution));
	}

	@Transactional
	private void processDistributionItem(MedicineDistribution distribution, MedicineDistributionItemDTO itemDTO) {
		// Validate medicine exists
		if (itemDTO.getMedicine() == null || itemDTO.getMedicine().getId() == null) {
			throw new IllegalArgumentException("Medicine ID is required");
		}

		if (!medicineRepository.existsById(itemDTO.getMedicine().getId())) {
			throw new EntityNotFoundException("Medicine not found with ID: " + itemDTO.getMedicine().getId());
		}

		// Validate batch ID is provided
		if (itemDTO.getBatch() == null || itemDTO.getBatch().getId() == null) {
			throw new IllegalArgumentException("Batch ID is required for medicine distribution");
		}

		// Check available quantity in batch
		MedicinePurchaseBatch batch = medicinePurchaseBatchRepository.findById(itemDTO.getBatch().getId())
				.orElseThrow(() -> new EntityNotFoundException("Batch not found with ID: " + itemDTO.getBatch().getId()));

		if (batch.getCurrentQuantity() < itemDTO.getQuantity()) {
			throw new IllegalArgumentException("Insufficient quantity in batch. Available: " +
				batch.getCurrentQuantity() + ", Requested: " + itemDTO.getQuantity());
		}

		// Check if item already exists for this medicine in this distribution
		Optional<MedicineDistributionItem> existingItem = distributionItemRepository
				.findByDistributionIdAndMedicineId(distribution.getId(), itemDTO.getMedicine().getId());

		MedicineDistributionItem distributionItem;
		int oldQuantity = 0;

		if (existingItem.isPresent()) {
			// Update existing item
			distributionItem = existingItem.get();
			oldQuantity = distributionItem.getQuantity();
			distributionItem.setQuantity(itemDTO.getQuantity());
			distributionItem.setTotalPrice(itemDTO.getTotalPrice());
			distributionItem.setUnitPrice(itemDTO.getUnitPrice());
		} else {
			// Create new item
			distributionItem = new MedicineDistributionItem();
			distributionItem.setDistribution(distribution);
			distributionItem.setMedicine(medicineRepository.findById(itemDTO.getMedicine().getId()).get());
			distributionItem.setBatch(batch);
			distributionItem.setQuantity(itemDTO.getQuantity());
			distributionItem.setUnitPrice(itemDTO.getUnitPrice());
			distributionItem.setTotalPrice(itemDTO.getTotalPrice());
		}

		// Save the distribution item
		distributionItemRepository.save(distributionItem);

		// Update batch quantity (reduce inventory)
		int quantityDifference = itemDTO.getQuantity() - oldQuantity;
		batch.setCurrentQuantity(batch.getCurrentQuantity() - quantityDifference);
		medicinePurchaseBatchRepository.save(batch);
	}

	@Override
	public MedicineDistributionDTO update(Long id, MedicineDistributionDTO dto) {
		MedicineDistribution entity = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("MedicineDistribution not found with ID: " + id));
		entity.setPatient(patientDetailRepository.findById(dto.getPatientId())
				.orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + dto.getPatientId())));
		entity.setDeliveryCenter(deliveryCenterRepository.findById(dto.getDeliveryCenterId()).orElseThrow(
				() -> new EntityNotFoundException("DeliveryCenter not found with ID: " + dto.getDeliveryCenterId())));
		mapper.updateEntityFromDto(dto, entity);
		return mapper.toDTO(repository.save(entity));
	}

	@Override
	public void delete(Long id) {
		MedicineDistribution entity = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("MedicineDistribution not found with ID: " + id));
		repository.delete(entity);
	}

	@Override
	public MedicineDistributionDTO getById(Long id) {
		return repository.findById(id).map(mapper::toDTO)
				.orElseThrow(() -> new EntityNotFoundException("MedicineDistribution not found with ID: " + id));
	}

	@Override
	public List<MedicineDistributionDTO> getAll() {
		return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
	}
}
