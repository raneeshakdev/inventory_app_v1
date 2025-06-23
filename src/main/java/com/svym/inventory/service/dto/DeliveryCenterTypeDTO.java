package com.svym.inventory.service.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryCenterTypeDTO {

    private Long id;

    @NotBlank(message = "Type name is mandatory")
    @Size(max = 100, message = "Type name can have at most 100 characters")
    private String typeName;

    @Size(max = 255, message = "Description can have at most 255 characters")
    private String description;
}
