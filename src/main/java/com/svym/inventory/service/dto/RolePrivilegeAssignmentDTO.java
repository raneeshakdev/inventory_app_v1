package com.svym.inventory.service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class RolePrivilegeAssignmentDTO {

    @NotNull(message = "Role ID is required")
    @Positive(message = "Role ID must be positive")
    private Long roleId;

    @NotEmpty(message = "At least one privilege ID is required")
    private List<@NotNull @Positive Long> privilegeIds;

    // Default constructor
    public RolePrivilegeAssignmentDTO() {}

    // Constructor with parameters
    public RolePrivilegeAssignmentDTO(Long roleId, List<Long> privilegeIds) {
        this.roleId = roleId;
        this.privilegeIds = privilegeIds;
    }

    // Getters and setters
    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<Long> getPrivilegeIds() {
        return privilegeIds;
    }

    public void setPrivilegeIds(List<Long> privilegeIds) {
        this.privilegeIds = privilegeIds;
    }

    @Override
    public String toString() {
        return "RolePrivilegeAssignmentDTO{" +
                "roleId=" + roleId +
                ", privilegeIds=" + privilegeIds +
                '}';
    }
}
