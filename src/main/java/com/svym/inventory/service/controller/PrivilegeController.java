package com.svym.inventory.service.controller;

import com.svym.inventory.service.dto.PrivilegeRequestDTO;
import com.svym.inventory.service.dto.PrivilegeResponseDTO;
import com.svym.inventory.service.privilege.PrivilegeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/privileges")
@Validated
public class PrivilegeController {

    private static final Logger logger = LoggerFactory.getLogger(PrivilegeController.class);

    @Autowired
    private PrivilegeService privilegeService;

    /**
     * Create a new privilege
     * POST /api/v1/privileges
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPrivilege(@Valid @RequestBody PrivilegeRequestDTO requestDTO) {
        logger.info("Creating new privilege: {}", requestDTO.getPrivilegeName());

        try {
            PrivilegeResponseDTO privilege = privilegeService.createPrivilege(requestDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Privilege created successfully");
            response.put("data", privilege);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            logger.error("Error creating privilege: {}", e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Get privilege by ID
     * GET /api/v1/privileges/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPrivilegeById(@PathVariable @Positive Long id) {
        logger.info("Fetching privilege with ID: {}", id);

        try {
            PrivilegeResponseDTO privilege = privilegeService.getPrivilegeById(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", privilege);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error fetching privilege by ID {}: {}", id, e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Get all privileges
     * GET /api/v1/privileges
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPrivileges(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String searchType,
            @RequestParam(defaultValue = "false") boolean paginated,
            @PageableDefault(size = 20) Pageable pageable) {

        logger.info("Fetching privileges - search: {}, searchType: {}, paginated: {}", search, searchType, paginated);

        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);

            if (search != null && !search.trim().isEmpty()) {
                // Handle search functionality
                List<PrivilegeResponseDTO> privileges;
                if ("description".equalsIgnoreCase(searchType)) {
                    privileges = privilegeService.searchPrivilegesByDescription(search.trim());
                } else {
                    // Default search by name
                    privileges = privilegeService.searchPrivilegesByName(search.trim());
                }
                response.put("message", "Search results for: " + search);
                response.put("data", privileges);
                response.put("totalElements", privileges.size());
            } else if (paginated) {
                // Handle pagination
                Page<PrivilegeResponseDTO> privilegePage = privilegeService.getAllPrivileges(pageable);
                response.put("data", privilegePage.getContent());
                response.put("totalElements", privilegePage.getTotalElements());
                response.put("totalPages", privilegePage.getTotalPages());
                response.put("currentPage", privilegePage.getNumber());
                response.put("pageSize", privilegePage.getSize());
            } else {
                // Get all privileges without pagination
                List<PrivilegeResponseDTO> privileges = privilegeService.getAllPrivileges();
                response.put("data", privileges);
                response.put("totalElements", privileges.size());
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching privileges: {}", e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error fetching privileges: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Update privilege
     * PUT /api/v1/privileges/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePrivilege(
            @PathVariable @Positive Long id,
            @Valid @RequestBody PrivilegeRequestDTO requestDTO) {

        logger.info("Updating privilege with ID: {}", id);

        try {
            PrivilegeResponseDTO privilege = privilegeService.updatePrivilege(id, requestDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Privilege updated successfully");
            response.put("data", privilege);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error updating privilege with ID {}: {}", id, e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            HttpStatus status = e.getMessage().contains("not found") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(errorResponse);
        }
    }

    /**
     * Delete privilege
     * DELETE /api/v1/privileges/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePrivilege(@PathVariable @Positive Long id) {
        logger.info("Deleting privilege with ID: {}", id);

        try {
            privilegeService.deletePrivilege(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Privilege deleted successfully");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error deleting privilege with ID {}: {}", id, e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Check if privilege exists
     * GET /api/v1/privileges/exists/{privilegeName}
     */
    @GetMapping("/exists/{privilegeName}")
    public ResponseEntity<Map<String, Object>> checkPrivilegeExists(@PathVariable @NotBlank String privilegeName) {
        logger.info("Checking if privilege exists: {}", privilegeName);

        boolean exists = privilegeService.privilegeExists(privilegeName);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("exists", exists);
        response.put("privilegeName", privilegeName);

        return ResponseEntity.ok(response);
    }
}
