package com.svym.inventory.scheduler.medicinedailysummary;

import com.svym.inventory.service.entity.LocationAnalytics;
import com.svym.inventory.service.repository.LocationAnalyticsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

@Service
public class LocationAnalyticsUpdateService {

    private static final Logger logger = LoggerFactory.getLogger(LocationAnalyticsUpdateService.class);

    @Autowired
    private LocationAnalyticsRepository locationAnalyticsRepository;

    @Transactional
    public void updateLocationAnalytics(LocalDate summaryDate) {
        logger.info("Updating location analytics for date: {}", summaryDate);

        // Get daily purchase data by location
        List<Object[]> dailyPurchaseData = locationAnalyticsRepository.getDailyPurchaseByLocation(summaryDate);

        // Calculate time periods
        int year = summaryDate.getYear();
        int weekNumber = summaryDate.get(WeekFields.of(Locale.getDefault()).weekOfYear());
        String monthName = getShortMonthName(summaryDate.getMonth());

        logger.info("Processing {} locations for year: {}, month: {}, week: {}",
                   dailyPurchaseData.size(), year, monthName, weekNumber);

        for (Object[] data : dailyPurchaseData) {
            Long locationId = (Long) data[0];
            // Fix the casting issue - handle different numeric types that might be returned
            BigDecimal dailyPurchaseAmount = convertToBigDecimal(data[1]);

            // Find existing analytics record for this location and time period
            LocationAnalytics existingRecord = locationAnalyticsRepository
                .findByLocationAndPeriod(locationId, year, weekNumber, monthName);

            if (existingRecord != null) {
                // Update existing record - add to the totals
                BigDecimal newDailyTotal = existingRecord.getTotalPurchaseAmount().add(dailyPurchaseAmount);
                BigDecimal newWeeklyTotal = existingRecord.getWeeklyTotalPurchase().add(dailyPurchaseAmount);

                existingRecord.setTotalPurchaseAmount(newDailyTotal);
                existingRecord.setWeeklyTotalPurchase(newWeeklyTotal);
                existingRecord.setUpdatedAt(LocalDateTime.now());

                locationAnalyticsRepository.save(existingRecord);

                logger.debug("Updated existing location analytics for location {}: daily total {} -> {}, weekly total {} -> {}",
                           locationId,
                           existingRecord.getTotalPurchaseAmount().subtract(dailyPurchaseAmount), newDailyTotal,
                           existingRecord.getWeeklyTotalPurchase().subtract(dailyPurchaseAmount), newWeeklyTotal);
            } else {
                // Create new analytics record
                LocationAnalytics newRecord = new LocationAnalytics();
                newRecord.setLocationId(locationId);
                newRecord.setAnalyticsDate(summaryDate);
                newRecord.setTotalPurchaseAmount(dailyPurchaseAmount);
                newRecord.setWeeklyTotalPurchase(dailyPurchaseAmount);
                newRecord.setMonthName(monthName);
                newRecord.setYearNumber(year);
                newRecord.setWeekNumber(weekNumber);
                newRecord.setCreatedAt(LocalDateTime.now());
                newRecord.setUpdatedAt(LocalDateTime.now());

                locationAnalyticsRepository.save(newRecord);

                logger.debug("Created new location analytics record for location {} with purchase amount: {}",
                           locationId, dailyPurchaseAmount);
            }
        }

        logger.info("Successfully updated location analytics for {} locations", dailyPurchaseData.size());
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

    private String getShortMonthName(Month month) {
        return switch (month) {
            case JANUARY -> "Jan";
            case FEBRUARY -> "Feb";
            case MARCH -> "Mar";
            case APRIL -> "Apr";
            case MAY -> "May";
            case JUNE -> "Jun";
            case JULY -> "Jul";
            case AUGUST -> "Aug";
            case SEPTEMBER -> "Sep";
            case OCTOBER -> "Oct";
            case NOVEMBER -> "Nov";
            case DECEMBER -> "Dec";
        };
    }
}
