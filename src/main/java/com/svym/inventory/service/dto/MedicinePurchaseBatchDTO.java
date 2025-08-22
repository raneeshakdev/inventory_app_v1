package com.svym.inventory.service.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicinePurchaseBatchDTO {

    private Long id;

    @NotBlank(message = "Batch name is required")
    private String batchName;

    @NotNull(message = "Medicine ID is required")
    private Long medicineId;

    @NotNull(message = "Expiry date is required")
    private LocalDateTime expiryDate;

    @NotNull(message = "Initial quantity is required")
    private Integer initialQuantity;

    @NotNull(message = "Current quantity is required")
    private Integer currentQuantity;

    @NotNull(message = "Total price is required")
    private Double totalPrice;

    @NotNull(message = "Unit price is required")
    private Double unitPrice;

    @NotNull(message = "Purchase type ID is required")
    private Long purchaseTypeId;

    @NotNull(message = "Location ID is required")
    private Long locationId;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    private LocalDateTime createdAt;
    
    private String createdBy;
    
    private LocalDateTime lastUpdatedAt;
    
    private String lastModifiedBy; 

}
