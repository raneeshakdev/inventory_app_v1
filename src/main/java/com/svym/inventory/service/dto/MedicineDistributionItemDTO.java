package com.svym.inventory.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDistributionItemDTO {

    private Long id;

    @NotNull(message = "Medicine cannot be null")
    private MedicineDTO medicine;

    @NotNull(message = "Batch cannot be null")
    private BatchDTO batch;

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    @NotNull(message = "Unit price cannot be null")
    @Positive(message = "Unit price must be positive")
    private Double unitPrice;

    @NotNull(message = "Total price cannot be null")
    @Positive(message = "Total price must be positive")
    private Double totalPrice;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MedicineDTO {
        @NotNull(message = "Medicine ID cannot be null")
        private Long id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BatchDTO {
        @NotNull(message = "Batch ID cannot be null")
        private Long id;
    }
}
