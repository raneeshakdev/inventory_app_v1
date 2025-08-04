package com.svym.inventory.service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svym.inventory.service.payload.request.ChangePasswordRequest;
import com.svym.inventory.service.payload.request.LoginRequest;
import com.svym.inventory.service.payload.request.SignupRequest;
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
}
