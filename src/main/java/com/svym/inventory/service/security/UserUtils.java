package com.svym.inventory.service.security;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;

import com.svym.inventory.service.entity.Role;
import com.svym.inventory.service.security.services.UserDetailsImpl;

public class UserUtils {
	
	public static UserDetailsImpl getCurrentUser() {
		return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	public static List<Role> getCurrentUserRoles() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toList()
				.stream()
				.map(role -> new Role())
				.toList();
	}

}
