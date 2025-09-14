package com.svym.inventory.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for medicine action items details view data transfer.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicineActionItemsDetailsDTO {

    private Long actionItemId;
    private Long medicineId;
    private String medicineName;
    private String actionType;
    private String stockCheckDetails;
    private LocalDateTime dateOfActionGenerated;
    private Long locationId;
    private LocalDateTime updatedAt;
    private Long typeId;
}
