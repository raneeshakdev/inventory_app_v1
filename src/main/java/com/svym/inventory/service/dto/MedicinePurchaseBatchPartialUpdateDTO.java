package com.svym.inventory.service.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicinePurchaseBatchPartialUpdateDTO {

    @NotBlank(message = "Batch name is required")
    private String batchName;

    @NotNull(message = "Expiry date is required")
    private LocalDateTime expiryDate;
}
