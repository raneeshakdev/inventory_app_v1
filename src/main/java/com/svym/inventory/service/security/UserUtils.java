package com.svym.inventory.service.security;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;

import com.svym.inventory.service.entity.Role;

public class UserUtils {
	
	public static String getCurrentUser() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	public static List<Role> getCurrentUserRoles() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toList()
				.stream()
				.map(role -> new Role())
				.toList();
	}

}
