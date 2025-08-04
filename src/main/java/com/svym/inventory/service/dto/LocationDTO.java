package com.svym.inventory.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {

	private Long id;

	@NotBlank(message = "Location name is mandatory")
	@Size(max = 100, message = "Location name must be less than 100 characters")
	private String name;

	@NotBlank(message = "Location address is mandatory")
	@Size(max = 255, message = "Location address must be less than 255 characters")
	private String locationAddress;

	@NotNull(message = "Image path cannot be null")
	@Size(max = 255, message = "Image path must be less than 255 characters")
	private String imagePath;

	@NotNull(message = "Is Active flag is mandatory")
	private Boolean isActive;

}
