package com.svym.inventory.service.dto;

import java.util.HashSet;
import java.util.Set;

import com.svym.inventory.service.entity.MedicineDistributionItem;

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

	@NotNull(message = "Distribution Type ID cannot be null")
	private Long distributionTypeId;

	@NotNull(message = "Delivery Center ID cannot be null")
	private Long deliveryCenterId;

	@NotNull(message = "Total items cannot be null")
	private Integer totalItems;

	@NotBlank(message = "Notes cannot be blank")
	private String notes;

	private String createdBy;

	@NotNull(message = "Distribution items cannot be null")
	private Set<MedicineDistributionItem> distributionItems = new HashSet<>();

}
