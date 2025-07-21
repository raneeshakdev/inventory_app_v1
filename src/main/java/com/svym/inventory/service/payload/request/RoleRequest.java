package com.svym.inventory.service.payload.request;

import java.util.Set;

import com.svym.inventory.service.entity.Role;

import lombok.Data;

@Data
public class RoleRequest {

	private Long userId;
	private Set<Role> roles;
}
