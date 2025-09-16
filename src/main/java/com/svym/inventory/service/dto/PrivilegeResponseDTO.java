package com.svym.inventory.service.dto;

public class PrivilegeResponseDTO {

    private Long id;
    private String privilegeName;
    private String description;

    // Default constructor
    public PrivilegeResponseDTO() {}

    // Constructor with parameters
    public PrivilegeResponseDTO(Long id, String privilegeName, String description) {
        this.id = id;
        this.privilegeName = privilegeName;
        this.description = description;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        return "PrivilegeResponseDTO{" +
                "id=" + id +
                ", privilegeName='" + privilegeName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
