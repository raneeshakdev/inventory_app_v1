package com.svym.inventory.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for MedicineDailyCostSummary entity.
 * Used for transferring medicine daily cost summary data between layers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDailyCostSummaryDTO {

    private Long id;
    private Long medicineId;
    private String medicineName;
    private Long locationId;
    private String locationName;
    private Long deliveryCenterId;
    private String deliveryCenterName;
    private LocalDate distDate;
    private Integer numberOfUnit;
    private Double totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Constructor without ID (for creating new records)
     */
    public MedicineDailyCostSummaryDTO(Long medicineId, String medicineName,
                                     Long locationId, String locationName,
                                     Long deliveryCenterId, String deliveryCenterName,
                                     LocalDate distDate, Integer numberOfUnit,
                                     Double totalPrice) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.locationId = locationId;
        this.locationName = locationName;
        this.deliveryCenterId = deliveryCenterId;
        this.deliveryCenterName = deliveryCenterName;
        this.distDate = distDate;
        this.numberOfUnit = numberOfUnit;
        this.totalPrice = totalPrice;
    }
}
