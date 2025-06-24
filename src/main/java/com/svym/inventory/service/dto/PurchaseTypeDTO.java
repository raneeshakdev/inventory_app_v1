package com.svym.inventory.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseTypeDTO {

    private Long id;

    private String typeName;

    private String description;
}
