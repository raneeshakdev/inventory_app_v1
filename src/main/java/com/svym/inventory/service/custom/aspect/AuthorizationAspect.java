package com.svym.inventory.service.custom.aspect;

import java.util.List;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;

import com.svym.inventory.service.custom.annotation.Authorization;
import com.svym.inventory.service.entity.Role;
import com.svym.inventory.service.security.UserUtils;

@Aspect
@Configuration
public class AuthorizationAspect {

	@Before(value = "@annotation(com.svym.inventory.service.custom.annotation.Authorization)")
	public void checkAuthorization(Authorization authorization) {
		List<Role> roles = UserUtils.getCurrentUserRoles();

		if (roles.stream().noneMatch(role -> authorization.access().getAccess().contains(role.getName().toString()))) {
			throw new AccessDeniedException("You do not have permission to access this resource.");
		}
	}
}
