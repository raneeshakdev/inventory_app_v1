package com.svym.inventory.service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svym.inventory.service.payload.request.ChangePasswordRequest;
import com.svym.inventory.service.payload.request.LoginRequest;
import com.svym.inventory.service.payload.request.SignupRequest;
import com.svym.inventory.service.payload.request.UserAddRequest;
import com.svym.inventory.service.payload.response.MessageResponse;
import com.svym.inventory.service.repository.UserRepository;
import com.svym.inventory.service.security.services.AuthServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

	private final UserRepository userRepository;

	private final AuthServiceImpl authService;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		return authService.authenticateUser(loginRequest);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}
		return authService.registerUser(signUpRequest);
		// return authService.newUserRequest(signUpRequest)
	}

	@PostMapping("/change-password")
	public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest request,
			Authentication authentication) {
		String username = authentication.getName();
		authService.changePassword(username, request);
		return ResponseEntity.ok("Password changed successfully");
	}

	@PostMapping("/add")
	public ResponseEntity<?> addUser(@RequestBody UserAddRequest userAddRequest) {
		if (Boolean.TRUE.equals(userRepository.existsByEmail(userAddRequest.getEmail()))) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}
		if (Boolean.TRUE.equals(userRepository.existsByFirstNameAndLastName(userAddRequest.getFirstName(), userAddRequest.getLastName()))) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: User already exists!"));
		}
		// Delegate password generation/encryption to the service
		return authService.addUser(userAddRequest);
	}

	@PostMapping("/reset/{user-id}")
	public ResponseEntity<?> resetPassword(@PathVariable("user-id") Long userId) {
		return authService.resetPassword(userId);
	}
}
