package com.svym.inventory.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationStatisticsDto {

    private Long id;
    private Long locationId;
    private String locationName;
    private String locationAddress;
    private Integer outOfStockCount;
    private Integer expiredCount;
    private Integer nearExpiryCount;
    private LocalDateTime lastUpdated;
    private Boolean isActive;
}
