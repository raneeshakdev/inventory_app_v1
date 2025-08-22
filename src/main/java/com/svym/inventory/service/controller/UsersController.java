package com.svym.inventory.service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svym.inventory.service.dto.UserDTO;
import com.svym.inventory.service.entity.Role;
import com.svym.inventory.service.payload.request.RoleRequest;
import com.svym.inventory.service.payload.request.UserAddRequest;
import com.svym.inventory.service.payload.request.UserUpdateRequest;
import com.svym.inventory.service.security.services.AuthServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UsersController {

	private final AuthServiceImpl authService;

	@PutMapping("/assignRole")
	public ResponseEntity<?> assignRole(@RequestBody RoleRequest roleRequest) {
		return authService.assignRole(roleRequest);
	}

	@GetMapping("/getAllRoles")
	public List<Role> getAllRoles() {
		return authService.getAllRoles();
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addUser(@RequestBody UserAddRequest  userAddRequest) {
		return authService.addUser(userAddRequest);
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		return authService.getAllUsers();
	}

	@PutMapping("/update")
	public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
		return authService.updateUser(userUpdateRequest);
	}
}
