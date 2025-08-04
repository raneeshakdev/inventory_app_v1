package com.svym.inventory.service.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.svym.inventory.service.entity.ERole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private Long id;

	private String firstName;

	private String lastName;

	private String email;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private Set<ERole> roles = new HashSet<>();

	private Boolean isTemporaryPwd;
}
