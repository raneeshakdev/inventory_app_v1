package com.svym.inventory.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDetailDTO {

    private Long id;

    // Assuming this is a unique identifier from an external system or internal patient ID
    private String patientId; // Changed to String as it might not be auto-generated ID

    @NotBlank(message = "Patient name is required")
    @Size(min = 2, max = 100, message = "Patient name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Patient address must not exceed 500 characters")
    private String address;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phone;

    @Pattern(regexp = "^$|\\d{10}", message = "Emergency contact number must be empty or 10 digits")
    private String emergencyContact;

    private Boolean isActive;
}
