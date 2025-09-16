package com.svym.inventory.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PrivilegeRequestDTO {

    @NotBlank(message = "Privilege name is required")
    @Size(max = 100, message = "Privilege name must not exceed 100 characters")
    private String privilegeName;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    // Default constructor
    public PrivilegeRequestDTO() {}

    // Constructor with parameters
    public PrivilegeRequestDTO(String privilegeName, String description) {
        this.privilegeName = privilegeName;
        this.description = description;
    }

    // Getters and setters
    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PrivilegeRequestDTO{" +
                "privilegeName='" + privilegeName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
