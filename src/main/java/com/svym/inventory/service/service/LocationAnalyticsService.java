package com.svym.inventory.service.service;

import com.svym.inventory.service.dto.MonthlyAnalyticsDto;
import com.svym.inventory.service.repository.LocationAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LocationAnalyticsService {

    private final LocationAnalyticsRepository locationAnalyticsRepository;

    public List<MonthlyAnalyticsDto> getMonthlyAnalytics(Long locationId, Integer year) {
        // If year is not provided, use current year
        Integer targetYear = (year != null) ? year : LocalDate.now().getYear();

        // Get data from database using the year
        List<Object[]> monthlyData = locationAnalyticsRepository.findMonthlyTotalsByLocationAndYear(
                locationId, targetYear);

        // Convert to map for easier lookup
        Map<String, BigDecimal> dataMap = new HashMap<>();
        for (Object[] row : monthlyData) {
            String month = (String) row[0];
            BigDecimal amount = (BigDecimal) row[1];
            dataMap.put(month, amount);
        }

        // Create response for all 12 months
        List<MonthlyAnalyticsDto> result = new ArrayList<>();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        BigDecimal previousValue = null;

        for (String month : months) {
            BigDecimal value = dataMap.getOrDefault(month, BigDecimal.ZERO);
            MonthlyAnalyticsDto dto = new MonthlyAnalyticsDto(month, value);

            // Calculate percentage change for May (as shown in your example)
            if ("May".equals(month) && previousValue != null && previousValue.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal changePercentage = value.subtract(previousValue)
                        .divide(previousValue, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));

                if (changePercentage.compareTo(BigDecimal.ZERO) > 0) {
                    dto.setChange("+" + changePercentage.setScale(0, RoundingMode.HALF_UP) + "%");
                } else if (changePercentage.compareTo(BigDecimal.ZERO) < 0) {
                    dto.setChange(changePercentage.setScale(0, RoundingMode.HALF_UP) + "%");
                }
            }

            result.add(dto);

            // Store previous value for change calculation (use April for May calculation)
            if ("Apr".equals(month)) {
                previousValue = value;
            }
        }

        return result;
    }
}
