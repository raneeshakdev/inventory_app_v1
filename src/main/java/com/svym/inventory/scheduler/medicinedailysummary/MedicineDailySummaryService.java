package com.svym.inventory.scheduler.medicinedailysummary;

import com.svym.inventory.service.entity.JobMedicineDailySummary;
import com.svym.inventory.service.repository.JobMedicineDailySummaryRepository;
import com.svym.inventory.service.medicinepbatch.MedicinePurchaseBatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicineDailySummaryService {

    private static final Logger logger = LoggerFactory.getLogger(MedicineDailySummaryService.class);

    @Autowired
    private MedicinePurchaseBatchRepository medicinePurchaseBatchRepository;

    @Autowired
    private JobMedicineDailySummaryRepository jobMedicineDailySummaryRepository;

    @Transactional
    public void generateAndSaveDailySummary(LocalDate summaryDate, LocalDate jobRunDate) {
        logger.info("Fetching daily summary data for date: {}", summaryDate);

        // Get aggregated data from medicine_purchase_batches
        List<Object[]> dailySummaryData = medicinePurchaseBatchRepository.getDailySummaryByMedicineType(summaryDate);

        logger.info("Found {} medicine types with data for date: {}", dailySummaryData.size(), summaryDate);

        // Process and save each summary record
        for (Object[] data : dailySummaryData) {
            Long medicineTypeId = (Long) data[0];
            Integer purchaseCount = ((Number) data[1]).intValue();
            Integer donationCount = ((Number) data[2]).intValue();

            JobMedicineDailySummary summary = new JobMedicineDailySummary();
            summary.setSummaryDate(summaryDate);
            summary.setJobRunDate(jobRunDate);
            summary.setMedicineTypeId(medicineTypeId);
            summary.setPurchaseCount(purchaseCount);
            summary.setDonationCount(donationCount);
            summary.setCreatedAt(LocalDateTime.now());
            summary.setUpdatedAt(LocalDateTime.now());

            jobMedicineDailySummaryRepository.save(summary);

            logger.debug("Saved summary for medicine type {}: purchases={}, donations={}",
                        medicineTypeId, purchaseCount, donationCount);
        }

        logger.info("Successfully saved daily summary data for {} medicine types", dailySummaryData.size());
    }
}
