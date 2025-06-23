package com.svym.inventory.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributionTypeDTO {

    private Long id;

    @NotBlank(message = "Distribution type code is mandatory")
    @Size(max = 50, message = "Code must not exceed 50 characters")
    private String typeName;

    @NotBlank(message = "Distribution type name is mandatory")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String description;
}
