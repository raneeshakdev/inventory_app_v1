package com.svym.inventory.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineDistributionItemDTO {

	private Long id;

	@NotNull(message = "Distribution ID cannot be null")
	private Long distributionId;

	@NotNull(message = "Medicine ID cannot be null")
	private Long medicineId;

	@NotNull(message = "Batch ID cannot be null")
	private Long batchId;

	@NotNull(message = "Quantity cannot be null")
	private Integer quantity;

	@NotBlank(message = "Unit Price cannot be null")
	private Double unitPrice;

	@NotNull(message = "Total Price cannot be null")
	private Double totalPrice;
}
