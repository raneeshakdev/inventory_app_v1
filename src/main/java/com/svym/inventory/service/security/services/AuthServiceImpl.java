package com.svym.inventory.service.security.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.svym.inventory.service.dto.UserDTO;
import com.svym.inventory.service.entity.ERole;
import com.svym.inventory.service.entity.Role;
import com.svym.inventory.service.entity.User;
import com.svym.inventory.service.payload.request.ChangePasswordRequest;
import com.svym.inventory.service.payload.request.LoginRequest;
import com.svym.inventory.service.payload.request.RoleRequest;
import com.svym.inventory.service.payload.request.SignupRequest;
import com.svym.inventory.service.payload.request.UserAddRequest;
import com.svym.inventory.service.payload.response.JwtResponse;
import com.svym.inventory.service.payload.response.MessageResponse;
import com.svym.inventory.service.repository.RoleRepository;
import com.svym.inventory.service.repository.UserRepository;
import com.svym.inventory.service.security.PasswordGenerator;
import com.svym.inventory.service.security.jwt.JwtUtils;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl {

	private final AuthenticationManager authenticationManager;

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final PasswordEncoder encoder;

	private final JwtUtils jwtUtils;

	public ResponseEntity<?> registerUser(@Valid SignupRequest signUpRequest) {
		// Create new user's account

		User user = renderUserToEntity(signUpRequest);

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_VIEWER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_DISTRIBUTION_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_VIEWER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	private User renderUserToEntity(@Valid SignupRequest signUpRequest) {
		User user = new User();
		user.setEmail(signUpRequest.getEmail());
		user.setFirstName(signUpRequest.getFirstName());
		user.setLastName(signUpRequest.getLastName());
		user.setPasswordHash(encoder.encode(signUpRequest.getPassword()));
		user.setIsActive(true);
		return user;
	}

	public ResponseEntity<?> authenticateUser(@Valid LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).toList();

		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getFirstName(),
				userDetails.getLastName(), userDetails.getEmail(), roles));
	}

	public void changePassword(String username, ChangePasswordRequest request) {
		User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new EntityNotFoundException("User not found"));

		if (!encoder.matches(request.getOldPassword(), user.getPasswordHash())) {
			throw new IllegalArgumentException("Old password is incorrect");
		}

		user.setPasswordHash(encoder.encode(request.getNewPassword()));
		userRepository.save(user);
	}

	public ResponseEntity<?> assignRole(RoleRequest roleRequest) {
		User user = userRepository.findById(roleRequest.getUserId())
				.orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + roleRequest.getUserId()));

		if (roleRequest.getRoles() == null || roleRequest.getRoles().isEmpty()) {
			throw new IllegalArgumentException("Role cannot be null or empty");
		}

		Set<Role> roles = new HashSet<>(roleRequest.getRoles());
		user.setRoles(roles);
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("Role assigned successfully!"));
	}

	public List<Role> getAllRoles() {
		return roleRepository.findAll();
	}

	public ResponseEntity<?> addUser(UserAddRequest userAddRequest) {
		if (Boolean.TRUE.equals(userRepository.existsByEmail(userAddRequest.getEmail()))) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}
		if (Boolean.TRUE.equals(userRepository.existsByFirstNameAndLastName(userAddRequest.getFirstName(),
				userAddRequest.getLastName()))) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: User already exists!"));
		}
		String tempPassword = PasswordGenerator.generateTempPassword();
		userAddRequest.setPassword(encoder.encode(tempPassword));

		User user = renderUserToEntity(userAddRequest);

		if (userAddRequest.getRoles() != null && !userAddRequest.getRoles().isEmpty()) {
			user.setRoles(userAddRequest.getRoles());
		} else {
			Role userRole = roleRepository.findByName(ERole.ROLE_VIEWER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			Set<Role> roles = new HashSet<>();
			roles.add(userRole);
			user.setRoles(roles);
		}
		user.setIsTemporaryPwd(true);

		userRepository.save(user);

		// Optionally send email with the temporary password & this will implement later
		log.info("Temporary Password: {}", tempPassword);
		return ResponseEntity
				.ok(new MessageResponse("User added successfully! Temporary password is: " + tempPassword));
	}

	private User renderUserToEntity(UserAddRequest signUpRequest) {
		User user = new User();
		user.setEmail(signUpRequest.getEmail());
		user.setFirstName(signUpRequest.getFirstName());
		user.setLastName(signUpRequest.getLastName());
		user.setPasswordHash(encoder.encode(signUpRequest.getPassword()));
		user.setIsActive(true);
		return user;
	}

	public ResponseEntity<List<UserDTO>> getAllUsers() {
		List<User> users = userRepository.findAll();
		List<UserDTO> userDTOs = users.stream().map(user -> {
			UserDTO dto = new UserDTO();
			dto.setId(user.getId());
			dto.setFirstName(user.getFirstName());
			dto.setLastName(user.getLastName());
			dto.setEmail(user.getEmail());
			dto.setCreatedAt(user.getCreatedAt());
			dto.setUpdatedAt(user.getUpdatedAt());
			dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
			dto.setIsTemporaryPwd(user.getIsTemporaryPwd());
			return dto;
		}).toList();
		return userDTOs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(userDTOs);
	}

}
