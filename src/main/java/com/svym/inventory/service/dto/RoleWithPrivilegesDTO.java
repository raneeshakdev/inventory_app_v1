package com.svym.inventory.service.dto;

import com.svym.inventory.service.entity.ERole;
import java.util.List;

public class RoleWithPrivilegesDTO {

    private Integer id;
    private ERole name;
    private String displayName;
    private List<PrivilegeResponseDTO> privileges;

    // Default constructor
    public RoleWithPrivilegesDTO() {}

    // Constructor with parameters
    public RoleWithPrivilegesDTO(Integer id, ERole name, String displayName, List<PrivilegeResponseDTO> privileges) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.privileges = privileges;
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

    public List<PrivilegeResponseDTO> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<PrivilegeResponseDTO> privileges) {
        this.privileges = privileges;
    }

    @Override
    public String toString() {
        return "RoleWithPrivilegesDTO{" +
                "id=" + id +
                ", name=" + name +
                ", displayName='" + displayName + '\'' +
                ", privileges=" + privileges +
                '}';
    }
}
