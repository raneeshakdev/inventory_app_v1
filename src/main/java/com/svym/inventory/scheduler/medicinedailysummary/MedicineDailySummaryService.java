package com.svym.inventory.scheduler.medicinedailysummary;

import com.svym.inventory.service.entity.JobMedicineDailySummary;
import com.svym.inventory.service.repository.JobMedicineDailySummaryRepository;
import com.svym.inventory.service.medicinepbatch.MedicinePurchaseBatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Autowired
    private LocationMedicineAnalyticsService locationMedicineAnalyticsService;

    @Autowired
    private LocationAnalyticsUpdateService locationAnalyticsUpdateService;

    @Transactional
    public void generateAndSaveDailySummary(LocalDate summaryDate, LocalDate jobRunDate) {
        logger.info("Fetching daily summary data for date: {}", summaryDate);

        // Get aggregated data from medicine_purchase_batches - now grouped by purchase type
        List<Object[]> dailySummaryData = medicinePurchaseBatchRepository.getDailySummaryByPurchaseType(summaryDate);

        logger.info("Found {} purchase types with data for date: {}", dailySummaryData.size(), summaryDate);

        // Process and save each summary record
        for (Object[] data : dailySummaryData) {
            Long purchaseTypeId = (Long) data[0];

            // Handle different numeric types that might be returned from the database
            Integer purchaseCount = convertToInteger(data[1]);
            Integer donationCount = convertToInteger(data[2]);
            BigDecimal totalAmount = convertToBigDecimal(data[3]);

            JobMedicineDailySummary summary = new JobMedicineDailySummary();
            summary.setSummaryDate(summaryDate);
            summary.setJobRunDate(jobRunDate);
            summary.setPurchaseTypeId(purchaseTypeId);
            summary.setPurchaseCount(purchaseCount);
            summary.setDonationCount(donationCount);
            summary.setTotalAmount(totalAmount);
            summary.setCreatedAt(LocalDateTime.now());
            summary.setUpdatedAt(LocalDateTime.now());

            jobMedicineDailySummaryRepository.save(summary);

            logger.debug("Saved summary for purchase type {}: purchases={}, donations={}, total={}",
                        purchaseTypeId, purchaseCount, donationCount, totalAmount);
        }

        logger.info("Successfully saved daily summary data for {} purchase types", dailySummaryData.size());

        // Update location medicine analytics after saving daily summary
        logger.info("Starting location medicine analytics update for date: {}", summaryDate);
        locationMedicineAnalyticsService.updateLocationMedicineAnalytics(summaryDate);
        logger.info("Completed location medicine analytics update");

        // Update location analytics after medicine analytics
        logger.info("Starting location analytics update for date: {}", summaryDate);
        locationAnalyticsUpdateService.updateLocationAnalytics(summaryDate);
        logger.info("Completed location analytics update");
    }

    /**
     * Safely converts various numeric types to Integer
     * Handles Long, BigDecimal, Double, Integer, and other Number types
     */
    private Integer convertToInteger(Object value) {
        if (value == null) {
            return 0;
        }

        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Long) {
            return ((Long) value).intValue();
        } else if (value instanceof BigDecimal) {
            return ((BigDecimal) value).intValue();
        } else if (value instanceof Double) {
            return ((Double) value).intValue();
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        } else {
            // Try to parse as string if it's not a Number
            try {
                return Integer.parseInt(value.toString());
            } catch (NumberFormatException e) {
                logger.warn("Could not convert value {} to Integer, defaulting to 0", value);
                return 0;
            }
        }
    }

    /**
     * Safely converts various numeric types to BigDecimal
     * Handles BigDecimal, Double, Float, Integer, and other Number types
     */
    private BigDecimal convertToBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }

        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Double) {
            return BigDecimal.valueOf((Double) value);
        } else if (value instanceof Float) {
            return BigDecimal.valueOf((Float) value);
        } else if (value instanceof Integer) {
            return BigDecimal.valueOf((Integer) value);
        } else if (value instanceof Long) {
            return BigDecimal.valueOf((Long) value);
        } else if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        } else {
            // Try to parse as string if it's not a Number
            try {
                return new BigDecimal(value.toString());
            } catch (NumberFormatException e) {
                logger.warn("Could not convert value {} to BigDecimal, defaulting to 0", value);
                return BigDecimal.ZERO;
            }
        }
    }
}
