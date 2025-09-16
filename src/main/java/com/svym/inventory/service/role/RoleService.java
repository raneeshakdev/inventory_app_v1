package com.svym.inventory.service.role;

import com.svym.inventory.service.dto.*;
import com.svym.inventory.service.entity.Role;
import com.svym.inventory.service.entity.RolePrivilege;
import com.svym.inventory.service.repository.RoleRepository;
import com.svym.inventory.service.repository.RolePrivilegeRepository;
import com.svym.inventory.service.privilege.PrivilegeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RolePrivilegeRepository rolePrivilegeRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    /**
     * Get all roles
     */
    @Transactional(readOnly = true)
    public List<RoleResponseDTO> getAllRoles() {
        logger.info("Fetching all roles");

        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(this::mapToRoleResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get role by ID
     */
    @Transactional(readOnly = true)
    public RoleResponseDTO getRoleById(Long roleId) {
        logger.info("Fetching role with ID: {}", roleId);

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));

        return mapToRoleResponseDTO(role);
    }

    /**
     * Assign multiple privileges to a role
     */
    public void assignPrivilegesToRole(RolePrivilegeAssignmentDTO assignmentDTO) {
        logger.info("Assigning {} privileges to role ID: {}",
                   assignmentDTO.getPrivilegeIds().size(), assignmentDTO.getRoleId());

        // Verify role exists
        if (!roleRepository.existsById(assignmentDTO.getRoleId())) {
            throw new RuntimeException("Role not found with ID: " + assignmentDTO.getRoleId());
        }

        // Verify all privileges exist
        for (Long privilegeId : assignmentDTO.getPrivilegeIds()) {
            if (!privilegeRepository.existsById(privilegeId)) {
                throw new RuntimeException("Privilege not found with ID: " + privilegeId);
            }
        }

        // Remove existing privileges for the role first (replace operation)
        rolePrivilegeRepository.deleteAllByRoleId(assignmentDTO.getRoleId());

        // Add new privilege assignments
        List<RolePrivilege> rolePrivileges = assignmentDTO.getPrivilegeIds().stream()
                .map(privilegeId -> new RolePrivilege(assignmentDTO.getRoleId(), privilegeId))
                .collect(Collectors.toList());

        rolePrivilegeRepository.saveAll(rolePrivileges);

        logger.info("Successfully assigned {} privileges to role ID: {}",
                   rolePrivileges.size(), assignmentDTO.getRoleId());
    }

    /**
     * Add single privilege to a role (without removing existing ones)
     */
    public void addPrivilegeToRole(Long roleId, Long privilegeId) {
        logger.info("Adding privilege ID {} to role ID: {}", privilegeId, roleId);

        // Verify role exists
        if (!roleRepository.existsById(roleId)) {
            throw new RuntimeException("Role not found with ID: " + roleId);
        }

        // Verify privilege exists
        if (!privilegeRepository.existsById(privilegeId)) {
            throw new RuntimeException("Privilege not found with ID: " + privilegeId);
        }

        // Check if association already exists
        if (rolePrivilegeRepository.existsByRoleIdAndPrivilegeId(roleId, privilegeId)) {
            throw new RuntimeException("Role already has this privilege assigned");
        }

        RolePrivilege rolePrivilege = new RolePrivilege(roleId, privilegeId);
        rolePrivilegeRepository.save(rolePrivilege);

        logger.info("Successfully added privilege ID {} to role ID: {}", privilegeId, roleId);
    }

    /**
     * Remove privilege from a role
     */
    public void removePrivilegeFromRole(Long roleId, Long privilegeId) {
        logger.info("Removing privilege ID {} from role ID: {}", privilegeId, roleId);

        // Verify association exists
        if (!rolePrivilegeRepository.existsByRoleIdAndPrivilegeId(roleId, privilegeId)) {
            throw new RuntimeException("Role does not have this privilege assigned");
        }

        rolePrivilegeRepository.deleteByRoleIdAndPrivilegeId(roleId, privilegeId);

        logger.info("Successfully removed privilege ID {} from role ID: {}", privilegeId, roleId);
    }

    /**
     * Get all privileges for a role
     */
    @Transactional(readOnly = true)
    public List<PrivilegeResponseDTO> getPrivilegesForRole(Long roleId) {
        logger.info("Fetching privileges for role ID: {}", roleId);

        // Verify role exists
        if (!roleRepository.existsById(roleId)) {
            throw new RuntimeException("Role not found with ID: " + roleId);
        }

        List<Object[]> privilegeData = rolePrivilegeRepository.findPrivilegeDetailsByRoleId(roleId);

        return privilegeData.stream()
                .map(data -> new PrivilegeResponseDTO(
                        (Long) data[0],      // privilege id
                        (String) data[1],    // privilege name
                        (String) data[2]     // description
                ))
                .collect(Collectors.toList());
    }

    /**
     * Get role with its privileges
     */
    @Transactional(readOnly = true)
    public RoleWithPrivilegesDTO getRoleWithPrivileges(Long roleId) {
        logger.info("Fetching role with privileges for role ID: {}", roleId);

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));

        List<PrivilegeResponseDTO> privileges = getPrivilegesForRole(roleId);

        return new RoleWithPrivilegesDTO(
                role.getId(),
                role.getName(),
                role.getDisplayName(),
                privileges
        );
    }

    /**
     * Get all roles with their privileges
     */
    @Transactional(readOnly = true)
    public List<RoleWithPrivilegesDTO> getAllRolesWithPrivileges() {
        logger.info("Fetching all roles with their privileges");

        List<Role> roles = roleRepository.findAll();

        return roles.stream()
                .map(role -> {
                    List<PrivilegeResponseDTO> privileges = getPrivilegesForRole(role.getId().longValue());
                    return new RoleWithPrivilegesDTO(
                            role.getId(),
                            role.getName(),
                            role.getDisplayName(),
                            privileges
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     * Map Role entity to RoleResponseDTO
     */
    private RoleResponseDTO mapToRoleResponseDTO(Role role) {
        return new RoleResponseDTO(
                role.getId(),
                role.getName(),
                role.getDisplayName()
        );
    }
}
