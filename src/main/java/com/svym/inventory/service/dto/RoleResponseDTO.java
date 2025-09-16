package com.svym.inventory.service.dto;

import com.svym.inventory.service.entity.ERole;

public class RoleResponseDTO {

    private Integer id;
    private ERole name;
    private String displayName;

    // Default constructor
    public RoleResponseDTO() {}

    // Constructor with parameters
    public RoleResponseDTO(Integer id, ERole name, String displayName) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "RoleResponseDTO{" +
                "id=" + id +
                ", name=" + name +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
