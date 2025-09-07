package com.svym.inventory.service.medicinedistribution;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.svym.inventory.service.deliverycenter.DeliveryCenterRepository;
import com.svym.inventory.service.dto.MedicineDistributionDTO;
import com.svym.inventory.service.dto.MedicineDistributionItemDTO;
import com.svym.inventory.service.dto.MedicineDistributionViewDTO;
import com.svym.inventory.service.entity.MedicineDistribution;
import com.svym.inventory.service.entity.MedicineDistributionItem;
import com.svym.inventory.service.entity.MedicineDistributionView;
import com.svym.inventory.service.entity.MedicineLocationStock;
import com.svym.inventory.service.entity.MedicineLocationStockId;
import com.svym.inventory.service.entity.MedicinePurchaseBatch;
import com.svym.inventory.service.entity.mapper.MedicineDistributionMapper;
import com.svym.inventory.service.medicineditem.MedicineDistributionItemRepository;
import com.svym.inventory.service.medicinepbatch.MedicinePurchaseBatchRepository;
import com.svym.inventory.service.patientdetail.PatientDetailRepository;
import com.svym.inventory.service.repository.MedicineRepository;
import com.svym.inventory.service.repository.MedicineDistributionViewRepository;
import com.svym.inventory.service.repository.MedicineLocationStockRepository;
import com.svym.inventory.service.medicinedailycostsummary.MedicineDailyCostSummaryService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Slf4j
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
	private final MedicineDistributionViewRepository medicineDistributionViewRepository;
	private final MedicineLocationStockRepository medicineLocationStockRepository;
	private final MedicineDailyCostSummaryService medicineDailyCostSummaryService;

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

		// Check if item already exists for this patient, medicine, and date (across all distributions)
		Optional<MedicineDistributionItem> existingItem = distributionItemRepository
				.findByPatientIdAndMedicineIdAndDistributionDate(
					distribution.getPatient().getId(),
					itemDTO.getMedicine().getId(),
					distribution.getDistributionDate());

		MedicineDistributionItem distributionItem;

		if (existingItem.isPresent()) {
			// Update existing item by accumulating quantities and prices
			distributionItem = existingItem.get();

			// Accumulate quantity and total price
			distributionItem.setQuantity(distributionItem.getQuantity() + itemDTO.getQuantity());
			distributionItem.setTotalPrice(distributionItem.getTotalPrice() + itemDTO.getTotalPrice());

			// Recalculate unit price based on accumulated values
			if (distributionItem.getQuantity() > 0) {
				distributionItem.setUnitPrice(distributionItem.getTotalPrice() / distributionItem.getQuantity());
			}
		} else {
			// Create new item
			distributionItem = new MedicineDistributionItem();
			distributionItem.setDistribution(distribution);
			distributionItem.setMedicine(medicineRepository.findById(itemDTO.getMedicine().getId())
				.orElseThrow(() -> new EntityNotFoundException("Medicine not found with ID: " + itemDTO.getMedicine().getId())));
			distributionItem.setBatch(batch);
			distributionItem.setQuantity(itemDTO.getQuantity());
			distributionItem.setUnitPrice(itemDTO.getUnitPrice());
			distributionItem.setTotalPrice(itemDTO.getTotalPrice());
		}

		// Save the distribution item
		distributionItemRepository.save(distributionItem);

		// Update batch quantity (reduce inventory by the new quantity being distributed)
		batch.setCurrentQuantity(batch.getCurrentQuantity() - itemDTO.getQuantity());
		medicinePurchaseBatchRepository.save(batch);

		// Update medicine location stock (reduce total number of medicines by the new quantity)
		updateMedicineLocationStock(itemDTO.getMedicine().getId(), batch.getLocation().getId(), itemDTO.getQuantity());

		// Create or update daily cost summary with the new quantity and price
		createOrUpdateDailyCostSummary(
			itemDTO.getMedicine().getId(),
			batch.getLocation().getId(),
			distribution.getDeliveryCenter().getId(),
			distribution.getDistributionDate(),
			itemDTO.getQuantity(),
			itemDTO.getTotalPrice()
		);
	}

	/**
	 * Creates or updates the daily cost summary for medicine distribution
	 * @param medicineId the ID of the medicine
	 * @param locationId the ID of the location
	 * @param deliveryCenterId the ID of the delivery center
	 * @param distributionDate the distribution date
	 * @param quantityDifference the quantity difference (positive for new/increased distribution)
	 * @param priceDifference the price difference for this distribution
	 */
	private void createOrUpdateDailyCostSummary(Long medicineId, Long locationId, Long deliveryCenterId,
			java.time.LocalDate distributionDate, int quantityDifference, Double priceDifference) {
		try {
			// Only create/update summary if there's actually a quantity difference
			if (quantityDifference != 0) {
				medicineDailyCostSummaryService.createOrUpdateSummary(
					medicineId,
					locationId,
					deliveryCenterId,
					distributionDate,
					quantityDifference,
					priceDifference
				);
			}
		} catch (Exception e) {
			// Log the error but don't fail the distribution process
			System.err.println("Failed to update daily cost summary: " + e.getMessage());
		}
	}

	/**
	 * Updates the MedicineLocationStock by reducing the totalNumberOfMedicines
	 * @param medicineId the ID of the medicine
	 * @param locationId the ID of the location
	 * @param quantityDifference the quantity to reduce (positive value means reduction)
	 */
	private void updateMedicineLocationStock(Long medicineId, Long locationId, int quantityDifference) {
		// Create composite key for MedicineLocationStock
		MedicineLocationStockId stockId = new MedicineLocationStockId();
		stockId.setMedicineId(medicineId);
		stockId.setLocationId(locationId);

		// Find existing stock record
		Optional<MedicineLocationStock> stockOptional = medicineLocationStockRepository.findById(stockId);

		if (stockOptional.isPresent()) {
			MedicineLocationStock stock = stockOptional.get();
			int currentTotal = stock.getTotalNumberOfMedicines();
			int newTotal = currentTotal - quantityDifference;

			// Ensure total doesn't go negative
			if (newTotal < 0) {
				throw new IllegalStateException("Cannot reduce medicine stock below zero. Current: " +
					currentTotal + ", Attempting to reduce by: " + quantityDifference);
			}

			stock.setTotalNumberOfMedicines(newTotal);

			// Update out of stock flag
			stock.setIsOutOfStock(newTotal == 0);

			// Update timestamp
			stock.setUpdatedAt(java.time.LocalDateTime.now());

			medicineLocationStockRepository.save(stock);
		} else {
			// Stock record doesn't exist - this might indicate a data inconsistency
			throw new EntityNotFoundException("MedicineLocationStock not found for medicineId: " +
				medicineId + " and locationId: " + locationId);
		}
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

	@Override
	public List<MedicineDistributionViewDTO> getMedicinesDistributedByPatient(Long deliveryCenterId, LocalDate distributionDate) {
		List<MedicineDistributionView> viewData = medicineDistributionViewRepository
			.findByDeliveryCenterIdAndDistributionDate(deliveryCenterId, distributionDate);

		// Group by patient
		Map<Long, List<MedicineDistributionView>> groupedByPatient = viewData.stream()
			.collect(Collectors.groupingBy(MedicineDistributionView::getPatientId));

		return groupedByPatient.entrySet().stream()
			.map(entry -> {
				List<MedicineDistributionView> patientMedicines = entry.getValue();
				MedicineDistributionView firstRecord = patientMedicines.get(0);

				// Convert each medicine distribution record to DTO
				List<MedicineDistributionViewDTO.MedicineDistributionItemViewDTO> medicines = patientMedicines.stream()
					.map(record -> new MedicineDistributionViewDTO.MedicineDistributionItemViewDTO(
						record.getDistributionId(),
						record.getMedicineId(),
						record.getMedicineName(),
						record.getBatchId(),
						record.getBatchName(),
						record.getLocationId(),
						record.getQuantity(),
						record.getUnitPrice(),
						record.getTotalPrice()
					))
					.collect(Collectors.toList());

				return new MedicineDistributionViewDTO(
					firstRecord.getPatientId(),
					firstRecord.getPatientExternalId(),
					firstRecord.getPatientName(),
					firstRecord.getDeliveryCenterId(),
					firstRecord.getDistributionDate(),
					medicines
				);
			})
			.collect(Collectors.toList());
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
}
