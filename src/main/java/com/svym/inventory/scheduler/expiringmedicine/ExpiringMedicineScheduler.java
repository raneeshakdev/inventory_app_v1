package com.svym.inventory.scheduler.expiringmedicine;

import com.svym.inventory.service.entity.*;
import com.svym.inventory.service.repository.MedicineActionItemsRepository;
import com.svym.inventory.service.repository.MedicineLocationStockRepository;
import com.svym.inventory.service.repository.MedicineRepository;
import com.svym.inventory.service.repository.LocationStatisticsRepository;
import com.svym.inventory.service.medicinepbatch.MedicinePurchaseBatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ExpiringMedicineScheduler {

	private static final Logger logger = LoggerFactory.getLogger(ExpiringMedicineScheduler.class);
	private static final int BATCH_SIZE = 100;

	@Autowired
	private MedicineActionItemsRepository medicineActionItemsRepository;

	@Autowired
	private MedicineRepository medicineRepository;

	@Autowired
	private MedicineLocationStockRepository medicineLocationStockRepository;

	@Autowired
	private MedicinePurchaseBatchRepository medicinePurchaseBatchRepository;

	@Autowired
	private LocationStatisticsRepository locationStatisticsRepository;

	// This method will be called every day at 12:15 AM
	@Scheduled(cron = "0 15 0 * * *")
	@Transactional
	public void checkExpiringMedicines() {
		logger.info("Starting medicine expiry and stock check scheduler at {}", LocalDateTime.now());

		try {
			// Step 1: Delete all entries from medicine_action_items
			logger.info("Deleting all existing action items");
			medicineActionItemsRepository.deleteAllActionItems();

			// Step 2: Reset medicine location stock tracking fields for fresh start
			logger.info("Resetting medicine location stock tracking fields");
			resetMedicineLocationStockFields();

			// Step 3: Get the list of medicines in batches
			int page = 0;
			Page<Medicine> medicinesPage;

			do {
				Pageable pageable = PageRequest.of(page, BATCH_SIZE);
				medicinesPage = medicineRepository.findAll(pageable);

				logger.info("Processing batch {} with {} medicines", page + 1, medicinesPage.getContent().size());

				for (Medicine medicine : medicinesPage.getContent()) {
					processMedicineStockAndExpiry(medicine);
				}

				page++;
			} while (medicinesPage.hasNext());

			// Step 4: Update location statistics based on action items
			logger.info("Updating location statistics");
			updateLocationStatistics();

			logger.info("Medicine expiry and stock check scheduler completed successfully");

		} catch (Exception e) {
			logger.error("Error occurred during medicine expiry and stock check: ", e);
			throw e;
		}
	}

	/**
	 * Reset medicine location stock tracking fields for a fresh start
	 */
	private void resetMedicineLocationStockFields() {
		try {
			LocalDateTime currentDate = LocalDateTime.now();

			// Get all medicine location stocks in batches
			int page = 0;
			Page<MedicineLocationStock> stockPage;

			do {
				Pageable pageable = PageRequest.of(page, BATCH_SIZE);
				stockPage = medicineLocationStockRepository.findAll(pageable);

				logger.info("Resetting stock fields for batch {} with {} records", page + 1, stockPage.getContent().size());

				for (MedicineLocationStock stock : stockPage.getContent()) {
					// Reset the tracking fields
					stock.setIsOutOfStock(false);
					stock.setHasExpiredBatches(false);
					stock.setNumberOfMedExpired(0);
					stock.setUpdatedAt(currentDate);
				}

				// Save the batch
				medicineLocationStockRepository.saveAll(stockPage.getContent());
				page++;

			} while (stockPage.hasNext());

			logger.info("Successfully reset medicine location stock tracking fields");

		} catch (Exception e) {
			logger.error("Error resetting medicine location stock fields: ", e);
			throw e;
		}
	}

	private void processMedicineStockAndExpiry(Medicine medicine) {
		try {
			// Get medicine location stock details for this medicine
			List<MedicineLocationStock> locationStocks = medicineLocationStockRepository
					.findByMedicineId(medicine.getId());

			for (MedicineLocationStock locationStock : locationStocks) {
				processLocationStock(medicine, locationStock);
			}

		} catch (Exception e) {
			logger.error("Error processing medicine ID {}: ", medicine.getId(), e);
		}
	}

	private void processLocationStock(Medicine medicine, MedicineLocationStock locationStock) {
		try {
			LocalDateTime currentDate = LocalDateTime.now();
			LocalDateTime twoWeeksFromNow = currentDate.plusWeeks(2);

			// Get expired batches for this medicine and location
			List<MedicinePurchaseBatch> expiredBatches = medicinePurchaseBatchRepository
					.findExpiredBatchesByMedicineAndLocation(medicine.getId(), locationStock.getLocationId(), currentDate);

			// Get near expiry batches (expiring in next 2 weeks, but not already expired)
			List<MedicinePurchaseBatch> nearExpiryBatches = medicinePurchaseBatchRepository
					.findNearExpiryBatchesByMedicineAndLocation(medicine.getId(), locationStock.getLocationId(),
							currentDate, twoWeeksFromNow);

			boolean hasExpiredBatches = !expiredBatches.isEmpty();
			boolean hasNearExpiryBatches = !nearExpiryBatches.isEmpty();
			int totalExpiredQuantity;
			int expiredBatchCount;

			if (hasExpiredBatches) {
				totalExpiredQuantity = expiredBatches.stream()
						.mapToInt(MedicinePurchaseBatch::getCurrentQuantity)
						.sum();
				expiredBatchCount = expiredBatches.size();

				// Update medicine location stock for expired medicines
				locationStock.setHasExpiredBatches(true);
				locationStock.setNumberOfMedExpired(totalExpiredQuantity);
				locationStock.setUpdatedAt(currentDate);

				// Create action item for expired medicines
				createActionItem(medicine, locationStock.getLocation(), "Expired",
						String.format("Expired batches: %d, Expired medicines: %d, Expired on: %s",
								expiredBatchCount, totalExpiredQuantity, currentDate.toLocalDate()));
			}

			// Check for near expiry medicines
			if (hasNearExpiryBatches) {
				int totalNearExpiryQuantity = nearExpiryBatches.stream()
						.mapToInt(MedicinePurchaseBatch::getCurrentQuantity)
						.sum();
				int nearExpiryBatchCount = nearExpiryBatches.size();

				// Find the earliest expiry date among near expiry batches
				LocalDateTime earliestExpiryDate = nearExpiryBatches.stream()
						.map(MedicinePurchaseBatch::getExpiryDate)
						.min(LocalDateTime::compareTo)
						.orElse(twoWeeksFromNow);

				// Create action item for near expiry medicines
				createActionItem(medicine, locationStock.getLocation(), "Near Expiry",
						String.format("Near expiry batches: %d, Near expiry medicines: %d, Earliest expiry: %s",
								nearExpiryBatchCount, totalNearExpiryQuantity, earliestExpiryDate.toLocalDate()));
			}

			// Check stock levels
			Integer totalMedicines = locationStock.getTotalNumberOfMedicines();
			Integer stockThreshold = medicine.getStockThreshold();
			Short numberOfBatches = locationStock.getNumberOfBatches();

			if (totalMedicines == 0 && numberOfBatches != null && numberOfBatches > 0) {
				// Out of stock - when no medicines are available
				locationStock.setIsOutOfStock(true);
				createActionItem(medicine, locationStock.getLocation(), "Out of Stock",
						String.format("No medicines available in stock (Batches: %d)", numberOfBatches));

			} else if (totalMedicines > 0 && totalMedicines < stockThreshold) {
				// Critically low stock
				createActionItem(medicine, locationStock.getLocation(), "Critically Low",
						String.format("Threshold: %d, Current stock: %d", stockThreshold, totalMedicines));
			}

			// Save updated location stock
			medicineLocationStockRepository.save(locationStock);

		} catch (Exception e) {
			logger.error("Error processing location stock for medicine ID {} and location ID {}: ",
					medicine.getId(), locationStock.getLocationId(), e);
		}
	}

	private void createActionItem(Medicine medicine, Location location, String actionType, String details) {
		try {
			MedicineActionItems actionItem = new MedicineActionItems();
			actionItem.setMedicine(medicine);
			actionItem.setLocation(location);
			actionItem.setActionType(actionType);
			actionItem.setStockCheckDetails(details);
			actionItem.setDateOfActionGenerated(LocalDateTime.now());
			actionItem.setUpdatedAt(LocalDateTime.now());

			medicineActionItemsRepository.save(actionItem);

			logger.debug("Created action item: {} for medicine {} at location {}",
					actionType, medicine.getMedicineName(), location.getId());

		} catch (Exception e) {
			logger.error("Error creating action item for medicine ID {} and location ID {}: ",
					medicine.getId(), location.getId(), e);
		}
	}

	private void updateLocationStatistics() {
		try {
			LocalDateTime currentDate = LocalDateTime.now();

			// Get all distinct location IDs that have action items
			List<Long> locationIds = medicineActionItemsRepository.findDistinctLocationIds();

			logger.info("Updating statistics for {} locations", locationIds.size());

			for (Long locationId : locationIds) {
				// Count different types of action items for this location
				Long outOfStockCount = medicineActionItemsRepository.countByLocationIdAndActionType(locationId, "Out of Stock");
				Long expiredCount = medicineActionItemsRepository.countByLocationIdAndActionType(locationId, "Expired");
				Long criticallyLowCount = medicineActionItemsRepository.countByLocationIdAndActionType(locationId, "Critically Low");
				Long nearExpiryCount = medicineActionItemsRepository.countByLocationIdAndActionType(locationId, "Near Expiry");

				// Find existing location statistics or create new one
				LocationStatistics locationStats = locationStatisticsRepository
						.findByLocationIdAndIsActiveTrue(locationId)
						.orElse(null);

				if (locationStats == null) {
					// Create new location statistics record
					locationStats = new LocationStatistics();
					Location location = new Location();
					location.setId(locationId);
					locationStats.setLocation(location);
					locationStats.setIsActive(true);
					locationStats.setCreatedAt(currentDate);
				}

				// Update the statistics
				locationStats.setStockStatusCount(outOfStockCount.intValue()); // Only Out of Stock count
				locationStats.setExpiredCount(expiredCount.intValue());
				locationStats.setNearExpiryCount(nearExpiryCount.intValue());
				locationStats.setLastUpdated(currentDate);
				locationStats.setUpdatedAt(currentDate);

				// Save the updated statistics
				locationStatisticsRepository.save(locationStats);

				logger.debug("Updated statistics for location {}: Stock Issues: {}, Expired: {}, Near Expiry: {}",
						locationId, locationStats.getStockStatusCount(), locationStats.getExpiredCount(),
						locationStats.getNearExpiryCount());
			}

			logger.info("Location statistics updated successfully for {} locations", locationIds.size());

		} catch (Exception e) {
			logger.error("Error updating location statistics: ", e);
			throw e;
		}
	}
}
