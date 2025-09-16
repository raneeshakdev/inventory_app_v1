package com.svym.inventory.service.controller;

import com.svym.inventory.service.dto.*;
import com.svym.inventory.service.role.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/roles")
@Validated
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    /**
     * Get all roles
     * GET /api/v1/roles
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllRoles() {
        logger.info("Fetching all roles");

        try {
            List<RoleResponseDTO> roles = roleService.getAllRoles();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", roles);
            response.put("totalElements", roles.size());
            response.put("message", "Roles fetched successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching roles: {}", e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error fetching roles: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get role by ID
     * GET /api/v1/roles/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getRoleById(@PathVariable @Positive Long id) {
        logger.info("Fetching role with ID: {}", id);

        try {
            RoleResponseDTO role = roleService.getRoleById(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", role);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error fetching role by ID {}: {}", id, e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Assign multiple privileges to a role (replaces existing privileges)
     * POST /api/v1/roles/assign-privileges
     */
    @PostMapping("/assign-privileges")
    public ResponseEntity<Map<String, Object>> assignPrivilegesToRole(
            @Valid @RequestBody RolePrivilegeAssignmentDTO assignmentDTO) {

        logger.info("Assigning privileges to role ID: {}", assignmentDTO.getRoleId());

        try {
            roleService.assignPrivilegesToRole(assignmentDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Privileges assigned to role successfully");
            response.put("roleId", assignmentDTO.getRoleId());
            response.put("assignedPrivileges", assignmentDTO.getPrivilegeIds().size());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error assigning privileges to role: {}", e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            HttpStatus status = e.getMessage().contains("not found") ?
                HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;

            return ResponseEntity.status(status).body(errorResponse);
        }
    }

    /**
     * Add single privilege to a role (without removing existing ones)
     * POST /api/v1/roles/{roleId}/privileges/{privilegeId}
     */
    @PostMapping("/{roleId}/privileges/{privilegeId}")
    public ResponseEntity<Map<String, Object>> addPrivilegeToRole(
            @PathVariable @Positive Long roleId,
            @PathVariable @Positive Long privilegeId) {

        logger.info("Adding privilege ID {} to role ID: {}", privilegeId, roleId);

        try {
            roleService.addPrivilegeToRole(roleId, privilegeId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Privilege added to role successfully");
            response.put("roleId", roleId);
            response.put("privilegeId", privilegeId);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error adding privilege to role: {}", e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            HttpStatus status = e.getMessage().contains("not found") ?
                HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;

            return ResponseEntity.status(status).body(errorResponse);
        }
    }

    /**
     * Remove privilege from a role
     * DELETE /api/v1/roles/{roleId}/privileges/{privilegeId}
     */
    @DeleteMapping("/{roleId}/privileges/{privilegeId}")
    public ResponseEntity<Map<String, Object>> removePrivilegeFromRole(
            @PathVariable @Positive Long roleId,
            @PathVariable @Positive Long privilegeId) {

        logger.info("Removing privilege ID {} from role ID: {}", privilegeId, roleId);

        try {
            roleService.removePrivilegeFromRole(roleId, privilegeId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Privilege removed from role successfully");
            response.put("roleId", roleId);
            response.put("privilegeId", privilegeId);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error removing privilege from role: {}", e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Get all privileges for a specific role
     * GET /api/v1/roles/{roleId}/privileges
     */
    @GetMapping("/{roleId}/privileges")
    public ResponseEntity<Map<String, Object>> getPrivilegesForRole(@PathVariable @Positive Long roleId) {
        logger.info("Fetching privileges for role ID: {}", roleId);

        try {
            List<PrivilegeResponseDTO> privileges = roleService.getPrivilegesForRole(roleId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", privileges);
            response.put("roleId", roleId);
            response.put("totalPrivileges", privileges.size());
            response.put("message", "Privileges fetched successfully for role");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error fetching privileges for role ID {}: {}", roleId, e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get role with all its privileges
     * GET /api/v1/roles/{roleId}/with-privileges
     */
    @GetMapping("/{roleId}/with-privileges")
    public ResponseEntity<Map<String, Object>> getRoleWithPrivileges(@PathVariable @Positive Long roleId) {
        logger.info("Fetching role with privileges for role ID: {}", roleId);

        try {
            RoleWithPrivilegesDTO roleWithPrivileges = roleService.getRoleWithPrivileges(roleId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", roleWithPrivileges);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error fetching role with privileges for ID {}: {}", roleId, e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get all roles with their privileges
     * GET /api/v1/roles/with-privileges
     */
    @GetMapping("/with-privileges")
    public ResponseEntity<Map<String, Object>> getAllRolesWithPrivileges() {
        logger.info("Fetching all roles with their privileges");

        try {
            List<RoleWithPrivilegesDTO> rolesWithPrivileges = roleService.getAllRolesWithPrivileges();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", rolesWithPrivileges);
            response.put("totalRoles", rolesWithPrivileges.size());
            response.put("message", "Roles with privileges fetched successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching roles with privileges: {}", e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error fetching roles with privileges: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
