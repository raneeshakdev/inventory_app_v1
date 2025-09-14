package com.svym.inventory.service.medicinepbatch;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.svym.inventory.service.dto.LocationMedicineStatusDTO;
import com.svym.inventory.service.dto.MedicinePurchaseBatchDTO;
import com.svym.inventory.service.dto.MedicinePurchaseBatchPartialUpdateDTO;
import com.svym.inventory.service.entity.LocationDonationStats;
import com.svym.inventory.service.entity.MedicineLocationStock;
import com.svym.inventory.service.entity.MedicineLocationStockId;
import com.svym.inventory.service.entity.MedicinePurchaseBatch;
import com.svym.inventory.service.location.LocationRepository;
import com.svym.inventory.service.purchasetype.PurchaseTypeRepository;
import com.svym.inventory.service.repository.LocationDonationStatsRepository;
import com.svym.inventory.service.repository.MedicineLocationStockRepository;
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
    private final LocationRepository locationRepository;
    private final MedicineLocationStockRepository medicineLocationStockRepository;
    private final LocationDonationStatsRepository locationDonationStatsRepository;

    @Override
    @Transactional
    public MedicinePurchaseBatchDTO create(MedicinePurchaseBatchDTO dto) {
        // Create and save the medicine purchase batch
        MedicinePurchaseBatch entity = mapToEntity(dto);
        MedicinePurchaseBatch savedEntity = repository.save(entity);

        // Update MedicineLocationStock
        updateMedicineLocationStock(dto.getMedicineId(), dto.getLocationId(), dto.getInitialQuantity());

        // Check if purchase type is Donation and update location donation stats
        if (isDonationPurchaseType(dto.getPurchaseTypeId())) {
            updateLocationDonationStats(dto.getLocationId(), BigDecimal.valueOf(dto.getTotalPrice()));
        }

        return mapToDTO(savedEntity);
    }

    private boolean isDonationPurchaseType(Long purchaseTypeId) {
        return purchaseTypeRepository.findById(purchaseTypeId)
            .map(purchaseType -> "Donation".equalsIgnoreCase(purchaseType.getTypeName()))
            .orElse(false);
    }

    private void updateLocationDonationStats(Long locationId, BigDecimal totalPrice) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int week = now.get(WeekFields.ISO.weekOfYear());

        // Check if there's an existing entry for location_id, year, month and week
        Optional<LocationDonationStats> existingStats = locationDonationStatsRepository
            .findByLocationIdAndYearAndMonthAndWeek(locationId, year, month, week);

        if (existingStats.isPresent()) {
            // Update existing entry by adding the new donation amount
            LocationDonationStats stats = existingStats.get();
            stats.setTotalDonation(stats.getTotalDonation().add(totalPrice));
            locationDonationStatsRepository.save(stats);
        } else {
            // Create new entry
            LocationDonationStats newStats = new LocationDonationStats();
            newStats.setLocationId(locationId);
            newStats.setYear(year);
            newStats.setMonth(month);
            newStats.setWeek(week);
            newStats.setTotalDonation(totalPrice);
            locationDonationStatsRepository.save(newStats);
        }
    }

    private void updateMedicineLocationStock(Long medicineId, Long locationId, Integer initialQuantity) {
        // Create composite key for MedicineLocationStock
        MedicineLocationStockId stockId = new MedicineLocationStockId();
        stockId.setMedicineId(medicineId);
        stockId.setLocationId(locationId);

        // Find existing record or create new one
        Optional<MedicineLocationStock> existingStock = medicineLocationStockRepository.findById(stockId);
        MedicineLocationStock medicineLocationStock;

        if (existingStock.isPresent()) {
            medicineLocationStock = existingStock.get();
        } else {
            // Create new record if it doesn't exist
            medicineLocationStock = new MedicineLocationStock();
            medicineLocationStock.setMedicineId(medicineId);
            medicineLocationStock.setLocationId(locationId);
            medicineLocationStock.setNumberOfBatches((short) 0);
            medicineLocationStock.setIsOutOfStock(true);
            medicineLocationStock.setHasExpiredBatches(false);
            medicineLocationStock.setTotalNumberOfMedicines(0);
            medicineLocationStock.setNumberOfMedExpired(0);
        }

        // Update the fields as requested
        // Increment numberOfBatches by one
        short currentBatches = medicineLocationStock.getNumberOfBatches();
        medicineLocationStock.setNumberOfBatches((short) (currentBatches + 1));

        // Increment totalNumberOfMedicines by initialQuantity
        Integer currentTotal = medicineLocationStock.getTotalNumberOfMedicines();
        medicineLocationStock.setTotalNumberOfMedicines(currentTotal + initialQuantity);

        // Update stock status - if we have medicines, it's not out of stock
        if (medicineLocationStock.getTotalNumberOfMedicines() > 0) {
            medicineLocationStock.setIsOutOfStock(false);
        }

        // Save the updated MedicineLocationStock
        medicineLocationStockRepository.save(medicineLocationStock);
    }

    @Override
    public MedicinePurchaseBatchDTO getById(Long id) {
        MedicinePurchaseBatch entity = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Batch not found"));
        return mapToDTO(entity);
    }

    @Override
    public List<MedicinePurchaseBatchDTO> getAll() {
        return repository.findAllNonDeleted().stream()
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
        existing.setLastModifiedBy(UserUtils.getCurrentUser().getId().toString());
        existing.setLastUpdatedAt(dto.getLastUpdatedAt());
     
        existing.setPurchaseType(purchaseTypeRepository.findById(dto.getPurchaseTypeId())
			.orElseThrow(() -> new EntityNotFoundException("Purchase Type not found")));
        existing.setMedicine(medicineRepository.findById(dto.getMedicineId())
			.orElseThrow(() -> new EntityNotFoundException("Medicine not found")));
        existing.setLocation(locationRepository.findById(dto.getLocationId())
			.orElseThrow(() -> new EntityNotFoundException("Location not found")));
        return mapToDTO(repository.save(existing));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Batch not found");
        }
        repository.deleteById(id);
    }

    @Override
    public void softDelete(Long id) {
        String currentUserId = UserUtils.getCurrentUser().getId().toString();
        int updatedRows = repository.softDeleteById(id, currentUserId);
        if (updatedRows == 0) {
            throw new EntityNotFoundException("Batch not found or already deleted");
        }
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
        dto.setLocationId(entity.getLocation().getId());
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
        entity.setCreatedBy(UserUtils.getCurrentUser().getId().toString()); // Assuming you have a UserContext to get the current user
        entity.setLastModifiedBy(UserUtils.getCurrentUser().getId().toString());
        if(dto.getPurchaseTypeId() != null) {
			entity.setPurchaseType(purchaseTypeRepository.findById(dto.getPurchaseTypeId())
				.orElseThrow(() -> new EntityNotFoundException("Purchase Type not found")));
		}
        if(dto.getMedicineId() != null) {
			entity.setMedicine(medicineRepository.findById(dto.getMedicineId())
				.orElseThrow(() -> new EntityNotFoundException("Medicine not found")));
		}
        if(dto.getLocationId() != null) {
			entity.setLocation(locationRepository.findById(dto.getLocationId())
				.orElseThrow(() -> new EntityNotFoundException("Location not found")));
		}
        return entity;
    }

	@Override
	public List<LocationMedicineStatusDTO> getLocationWiseStatus() {
		LocalDate cutoff = LocalDate.now().plusDays(30);
		return repository.fetchLocationWiseStatus(cutoff);
	}

	@Override
	public List<MedicinePurchaseBatchDTO> getAvailableBatchesByLocationAndMedicine(Long locationId, Long medicineId) {
		return repository.findAvailableBatchesByLocationAndMedicine(locationId, medicineId)
			.stream()
			.map(this::mapToDTO)
			.collect(Collectors.toList());
	}

	@Override
	public List<MedicinePurchaseBatchDTO> getAvailableBatchesByLocation(Long locationId) {
		return repository.findAvailableBatchesByLocation(locationId)
			.stream()
			.map(this::mapToDTO)
			.collect(Collectors.toList());
	}

	@Override
	public MedicinePurchaseBatchDTO partialUpdate(Long id, MedicinePurchaseBatchPartialUpdateDTO dto) {
		MedicinePurchaseBatch existing = repository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Batch not found"));

		// Only update the specified fields
		existing.setBatchName(dto.getBatchName());
		existing.setExpiryDate(dto.getExpiryDate());
		existing.setLastModifiedBy(UserUtils.getCurrentUser().getId().toString());
		existing.setLastUpdatedAt(LocalDateTime.now());

		return mapToDTO(repository.save(existing));
	}
}
