package com.svym.inventory.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicineDistributionItemDTO {

	private Long id;

	@NotBlank(message = "Distribution ID cannot be null")
	private Long distributionId;

	@NotBlank(message = "Medicine ID cannot be null")
	private Long medicineId;

	@NotBlank(message = "Batch ID cannot be null")
	private Long batchId;

	@NotBlank(message = "Quantity cannot be null")
	private Integer quantity;

	@NotBlank(message = "Unit Price cannot be null")
	private Double unitPrice;

	@NotBlank(message = "Total Price cannot be null")
	private Double totalPrice;
}
