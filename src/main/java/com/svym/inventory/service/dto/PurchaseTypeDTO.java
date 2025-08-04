package com.svym.inventory.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseTypeDTO {

    private Long id;

    @NotBlank(message = "Purchase type code is mandatory")
    private String typeName;

    @NotBlank(message = "Purchase type name is mandatory")
    private String description;
}
