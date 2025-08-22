package com.svym.inventory.service.payload.request;

import java.util.Set;

import com.svym.inventory.service.entity.Role;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    private Set<Role> roles;

    private Boolean isActive;
}
