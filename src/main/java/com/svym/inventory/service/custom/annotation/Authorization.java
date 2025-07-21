package com.svym.inventory.service.custom.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Authorization {
	public enum Access{
		ROLE_ADMIN("ROLE_ADMIN"),
		ROLE_VIEWER("ROLE_VIEWER", "ROLE_ADMIN", "ROLE_LOCATION_ADMIN", "ROLE_DISTRIBUTION_ADMIN", "ROLE_INVENTORY_ADMIN"),
		ROLE_LOCATION_ADMIN("ROLE_LOCATION_ADMIN", "ROLE_ADMIN", "ROLE_DISTRIBUTION_ADMIN", "ROLE_INVENTORY_ADMIN"),
		ROLE_INVENTORY_ADMIN("ROLE_INVENTORY_ADMIN", "ROLE_ADMIN", "ROLE_DISTRIBUTION_ADMIN"),
		ROLE_DISTRIBUTION_ADMIN("ROLE_DISTRIBUTION_ADMIN", "ROLE_ADMIN", "ROLE_INVENTORY_ADMIN");
		
		private String accessValue;

		public List<String> getAccess() {
			return Arrays.asList(accessValue.split(","));
		}

		private Access(String accessValue) {
			this.accessValue = accessValue;
		}
		private Access(String... accessValue) {
			this.accessValue = accessValue.length > 0 ? String.join(",", accessValue) : "";
		}
		
		
		
	}
	Access access() default Access.ROLE_VIEWER; // Default access level is READ
}
