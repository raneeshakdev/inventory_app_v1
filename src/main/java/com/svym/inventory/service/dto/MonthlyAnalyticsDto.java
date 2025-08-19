package com.svym.inventory.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyAnalyticsDto {
    private String month;
    private BigDecimal value;
    private String change;

    public MonthlyAnalyticsDto(String month, BigDecimal value) {
        this.month = month;
        this.value = value;
    }
}
