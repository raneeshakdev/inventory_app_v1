package com.svym.inventory.service.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDistributionDTO {

	private Long id;

	// Relationships
	@NotNull(message = "Patient ID cannot be null")
	private Long patientId;

	@NotNull(message = "Delivery Center ID cannot be null")
	private Long deliveryCenterId;

	private LocalDate distributionDate;

	private String createdBy;

	@NotNull(message = "Distribution items cannot be null")
	private List<MedicineDistributionItemDTO> distributionItems = new ArrayList<>();

}
