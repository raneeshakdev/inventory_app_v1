package com.svym.inventory.scheduler.medicinedailysummary;

import com.svym.inventory.service.entity.LocationMedicineAnalytics;
import com.svym.inventory.service.repository.LocationMedicineAnalyticsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

@Service
public class LocationMedicineAnalyticsService {

    private static final Logger logger = LoggerFactory.getLogger(LocationMedicineAnalyticsService.class);

    @Autowired
    private LocationMedicineAnalyticsRepository locationMedicineAnalyticsRepository;

    @Transactional
    public void updateLocationMedicineAnalytics(LocalDate summaryDate) {
        logger.info("Updating location medicine analytics for date: {}", summaryDate);

        // Get daily spend data by location and medicine type
        List<Object[]> dailySpendData = locationMedicineAnalyticsRepository.getDailySpendByLocationAndMedicineType(summaryDate);

        // Calculate time periods
        int year = summaryDate.getYear();
        int month = summaryDate.getMonthValue();
        int week = summaryDate.get(WeekFields.of(Locale.getDefault()).weekOfYear());

        logger.info("Processing {} location-medicine combinations for year: {}, month: {}, week: {}",
                   dailySpendData.size(), year, month, week);

        for (Object[] data : dailySpendData) {
            Long locationId = (Long) data[0];
            Long medicineTypeId = (Long) data[1];
            // Fix the casting issue - handle different numeric types that might be returned
            BigDecimal dailySpend = convertToBigDecimal(data[2]);

            // Find existing analytics record for this location, medicine type, and time period
            LocationMedicineAnalytics existingRecord = locationMedicineAnalyticsRepository
                .findByLocationMedicineTypeAndPeriod(locationId, medicineTypeId, year, month, week);

            if (existingRecord != null) {
                // Update existing record - add to the total spend
                BigDecimal newTotalSpend = existingRecord.getTotalSpend().add(dailySpend);
                existingRecord.setTotalSpend(newTotalSpend);
                existingRecord.setUpdatedAt(LocalDateTime.now());

                locationMedicineAnalyticsRepository.save(existingRecord);

                logger.debug("Updated existing analytics record for location {} and medicine type {}: {} -> {}",
                           locationId, medicineTypeId, existingRecord.getTotalSpend().subtract(dailySpend), newTotalSpend);
            } else {
                // Create new analytics record
                LocationMedicineAnalytics newRecord = new LocationMedicineAnalytics();
                newRecord.setLocationId(locationId);
                newRecord.setMedicineTypeId(medicineTypeId);
                newRecord.setTotalSpend(dailySpend);
                newRecord.setYear(year);
                newRecord.setMonth(month);
                newRecord.setWeek(week);
                newRecord.setCreatedAt(LocalDateTime.now());
                newRecord.setUpdatedAt(LocalDateTime.now());

                locationMedicineAnalyticsRepository.save(newRecord);

                logger.debug("Created new analytics record for location {} and medicine type {} with spend: {}",
                           locationId, medicineTypeId, dailySpend);
            }
        }

        logger.info("Successfully updated location medicine analytics for {} records", dailySpendData.size());
    }

    /**
     * Safely converts various numeric types to BigDecimal
     * Handles Double, BigDecimal, Long, Integer, and other Number types
     */
    private BigDecimal convertToBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }

        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Double) {
            return BigDecimal.valueOf((Double) value);
        } else if (value instanceof Long) {
            return BigDecimal.valueOf((Long) value);
        } else if (value instanceof Integer) {
            return BigDecimal.valueOf((Integer) value);
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
