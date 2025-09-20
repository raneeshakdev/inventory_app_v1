package com.svym.inventory.service.service;

import com.svym.inventory.service.dto.ExpenseReportDto;
import com.svym.inventory.service.repository.LocationMedicineAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ExpenseReportService {

    private final LocationMedicineAnalyticsRepository locationMedicineAnalyticsRepository;

    // Predefined colors for the chart
    private static final String[] COLORS = {
        "#dc2626", "#facc15", "#f97316", "#22c55e", "#3b82f6",
        "#8b5cf6", "#ec4899", "#06b6d4", "#84cc16", "#f59e0b"
    };

    public List<ExpenseReportDto> getExpenseReportByLocation(Long locationId) {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        List<Object[]> results = locationMedicineAnalyticsRepository
            .findExpensesByLocationAndMonth(locationId, currentMonth, currentYear);

        return mapToExpenseReportDto(results);
    }

    public List<ExpenseReportDto> getExpenseReportForAllLocations() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        List<Object[]> results = locationMedicineAnalyticsRepository
            .findExpensesByMonth(currentMonth, currentYear);

        return mapToExpenseReportDto(results);
    }

    public List<ExpenseReportDto> getExpenseReportByLocationAndMonth(Long locationId, int month, int year) {
        List<Object[]> results = locationMedicineAnalyticsRepository
            .findExpensesByLocationAndMonth(locationId, month, year);

        return mapToExpenseReportDto(results);
    }

    public List<ExpenseReportDto> getExpenseReportForAllLocationsByMonth(int month, int year) {
        List<Object[]> results = locationMedicineAnalyticsRepository
            .findExpensesByMonth(month, year);

        return mapToExpenseReportDto(results);
    }

    public List<ExpenseReportDto> getExpenseReportByLocationAndYear(Long locationId, int year) {
        List<Object[]> results = locationMedicineAnalyticsRepository
            .findExpensesByLocationAndYear(locationId, year);

        return mapToExpenseReportDto(results);
    }

    private List<ExpenseReportDto> mapToExpenseReportDto(List<Object[]> results) {
        return IntStream.range(0, results.size())
            .mapToObj(i -> {
                Object[] result = results.get(i);
                String name = (String) result[0];
                BigDecimal value = (BigDecimal) result[1];
                String color = COLORS[i % COLORS.length];

                return new ExpenseReportDto(name, value, color);
            })
            .toList();
    }
}
