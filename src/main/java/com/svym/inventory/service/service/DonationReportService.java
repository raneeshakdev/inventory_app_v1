package com.svym.inventory.service.service;

import com.svym.inventory.service.dto.ExpenseReportDto;
import com.svym.inventory.service.repository.LocationDonationStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DonationReportService {

    private final LocationDonationStatsRepository locationDonationStatsRepository;

    // Predefined colors for the chart (matching the required format)
    private static final String[] COLORS = {
        "bg-red-400", "bg-yellow-400", "bg-orange-400", "bg-green-400", 
        "bg-blue-400", "bg-purple-400", "bg-pink-400", "bg-cyan-400", 
        "bg-lime-400", "bg-amber-400"
    };

    public List<ExpenseReportDto> getDonationReportByLocation(Long locationId) {
        // Get the latest month/year with data instead of current system date
        Object[] latestMonthYear = getLatestAvailableMonthYear();
        Integer month = (Integer) latestMonthYear[0];
        Integer year = (Integer) latestMonthYear[1];

        List<Object[]> results = locationDonationStatsRepository
            .findCurrentMonthDonationsByLocation(locationId, month, year);

        return mapToDonationReportDto(results);
    }

    public List<ExpenseReportDto> getDonationReportForAllLocations() {
        // Get the latest month/year with data instead of current system date
        Object[] latestMonthYear = getLatestAvailableMonthYear();
        Integer month = (Integer) latestMonthYear[0];
        Integer year = (Integer) latestMonthYear[1];

        List<Object[]> results = locationDonationStatsRepository
            .findCurrentMonthDonationsForAllLocations(month, year);

        return mapToDonationReportDto(results);
    }

    public List<ExpenseReportDto> getDonationReportByLocationAndMonth(Long locationId, int month, int year) {
        List<Object[]> results = locationDonationStatsRepository
            .findDonationsByLocationAndMonth(locationId, month, year);

        return mapToDonationReportDto(results);
    }

    public List<ExpenseReportDto> getDonationReportForAllLocationsByMonth(int month, int year) {
        List<Object[]> results = locationDonationStatsRepository
            .findDonationsByMonth(month, year);

        return mapToDonationReportDto(results);
    }

    // Helper method to get the latest available month/year with data
    private Object[] getLatestAvailableMonthYear() {
        List<Object[]> latestData = locationDonationStatsRepository.findLatestMonthYear();

        if (latestData.isEmpty()) {
            // Fallback to current system date if no data exists
            LocalDateTime now = LocalDateTime.now();
            return new Object[]{now.getMonthValue(), now.getYear()};
        }

        return latestData.get(0);
    }

    // New method to get all available months/years
    public List<Object[]> getAvailableMonthsYears() {
        return locationDonationStatsRepository.findAllAvailableMonthsYears();
    }

    private List<ExpenseReportDto> mapToDonationReportDto(List<Object[]> results) {
        return IntStream.range(0, results.size())
            .mapToObj(i -> {
                Object[] result = results.get(i);
                String name = (String) result[0];
                BigDecimal amount = (BigDecimal) result[1];
                String color = COLORS[i % COLORS.length];

                return new ExpenseReportDto(name, amount, color);
            })
            .toList();
    }
}
