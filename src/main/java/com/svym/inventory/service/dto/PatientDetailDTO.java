package com.svym.inventory.service.dto;

import jakarta.validation.constraints.Email;
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

    @NotBlank(message = "Patient address is required")
    @Size(min = 10, max = 500, message = "Patient address must be between 10 and 500 characters")
    private String address;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phone;

    @Email(message = "Invalid email address")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{10}", message = "Emergency contact number must be 10 digits")
    private String emergencyContact;

    private String nextOfKin;

    private Boolean isActive;
}
