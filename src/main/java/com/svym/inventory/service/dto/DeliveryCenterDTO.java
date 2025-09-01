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
public class DeliveryCenterDTO {

	private Long id;
	
    @NotBlank(message = "Name is mandatory")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;
    
    @NotNull(message = "Location ID is required")
    private Long locationId;

    @NotBlank(message = "Contact phone is mandatory")
    private String contactPhone;

    @NotNull(message = "Type ID is required")
    private Long typeId;
    
    private Boolean isActive = true;
}
