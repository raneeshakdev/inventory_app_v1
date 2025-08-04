package com.svym.inventory.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineTypeDTO {

	private Long id;

	@NotBlank(message = "Name is mandatory")
	@Size(max = 50, message = "Name must be less than 100 characters")
	private String typeName;

	@NotBlank(message = "Description is mandatory")
	@Size(max = 500, message = "Description must be less than 500 characters")
	private String description;
}
