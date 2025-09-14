package com.svym.inventory.scheduler.expiringmedicine;

import com.svym.inventory.service.entity.*;
import com.svym.inventory.service.repository.MedicineActionItemsRepository;
import com.svym.inventory.service.repository.MedicineLocationStockRepository;
import com.svym.inventory.service.repository.MedicineRepository;
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

			// Get expired batches for this medicine and location
			List<MedicinePurchaseBatch> expiredBatches = medicinePurchaseBatchRepository
					.findExpiredBatchesByMedicineAndLocation(medicine.getId(), locationStock.getLocationId(), currentDate);

			boolean hasExpiredBatches = !expiredBatches.isEmpty();
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

			// Check stock levels - only check for 'Out of Stock' if number of batches > 1
			Integer totalMedicines = locationStock.getTotalNumberOfMedicines();
			Integer stockThreshold = medicine.getStockThreshold();
			Short numberOfBatches = locationStock.getNumberOfBatches();

			if (totalMedicines == 0 && numberOfBatches > 1) {
				// Out of stock - only when there are multiple batches
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
}
