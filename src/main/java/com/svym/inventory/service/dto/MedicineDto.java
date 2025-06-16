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
    private String name;
    private Long typeId;
    private String typeName;
    private Long userId;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime lastModifiedAt;
    private String lastModifiedBy;
    private Integer currentBatchesCount;
    private Integer stockThreshold;
    private Boolean outOfStock;
    private Boolean isActive;
}
