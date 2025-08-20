package com.svym.inventory.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicineDto {
    private Long id;
    private String medicineName;
    private Long typeId;
    private String typeName;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private Integer currentBatchesCount;
    private Integer stockThreshold;
    private Boolean outOfStock;
    private Boolean isActive;
}
