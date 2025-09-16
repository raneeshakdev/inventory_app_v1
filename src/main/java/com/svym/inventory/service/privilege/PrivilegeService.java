package com.svym.inventory.service.privilege;

import com.svym.inventory.service.dto.PrivilegeRequestDTO;
import com.svym.inventory.service.dto.PrivilegeResponseDTO;
import com.svym.inventory.service.entity.Privilege;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PrivilegeService {

    private static final Logger logger = LoggerFactory.getLogger(PrivilegeService.class);

    @Autowired
    private PrivilegeRepository privilegeRepository;

    /**
     * Create a new privilege
     */
    public PrivilegeResponseDTO createPrivilege(PrivilegeRequestDTO requestDTO) {
        logger.info("Creating new privilege with name: {}", requestDTO.getPrivilegeName());

        // Check if privilege already exists
        if (privilegeRepository.existsByPrivilegeName(requestDTO.getPrivilegeName())) {
            throw new RuntimeException("Privilege with name '" + requestDTO.getPrivilegeName() + "' already exists");
        }

        Privilege privilege = new Privilege();
        privilege.setPrivilegeName(requestDTO.getPrivilegeName());
        privilege.setDescription(requestDTO.getDescription());

        Privilege savedPrivilege = privilegeRepository.save(privilege);
        logger.info("Successfully created privilege with ID: {}", savedPrivilege.getId());

        return mapToResponseDTO(savedPrivilege);
    }

    /**
     * Get privilege by ID
     */
    @Transactional(readOnly = true)
    public PrivilegeResponseDTO getPrivilegeById(Long id) {
        logger.info("Fetching privilege with ID: {}", id);

        Privilege privilege = privilegeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Privilege not found with ID: " + id));

        return mapToResponseDTO(privilege);
    }

    /**
     * Get privilege by name
     */
    @Transactional(readOnly = true)
    public PrivilegeResponseDTO getPrivilegeByName(String privilegeName) {
        logger.info("Fetching privilege with name: {}", privilegeName);

        Privilege privilege = privilegeRepository.findByPrivilegeName(privilegeName)
                .orElseThrow(() -> new RuntimeException("Privilege not found with name: " + privilegeName));

        return mapToResponseDTO(privilege);
    }

    /**
     * Get all privileges
     */
    @Transactional(readOnly = true)
    public List<PrivilegeResponseDTO> getAllPrivileges() {
        logger.info("Fetching all privileges");

        List<Privilege> privileges = privilegeRepository.findAllOrderByPrivilegeName();
        return privileges.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get privileges with pagination
     */
    @Transactional(readOnly = true)
    public Page<PrivilegeResponseDTO> getAllPrivileges(Pageable pageable) {
        logger.info("Fetching privileges with pagination: page {}, size {}",
                   pageable.getPageNumber(), pageable.getPageSize());

        Page<Privilege> privilegePage = privilegeRepository.findAll(pageable);
        List<PrivilegeResponseDTO> privilegeDTOs = privilegePage.getContent().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(privilegeDTOs, pageable, privilegePage.getTotalElements());
    }

    /**
     * Update privilege
     */
    public PrivilegeResponseDTO updatePrivilege(Long id, PrivilegeRequestDTO requestDTO) {
        logger.info("Updating privilege with ID: {}", id);

        Privilege existingPrivilege = privilegeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Privilege not found with ID: " + id));

        // Check if the new name conflicts with another privilege
        if (!existingPrivilege.getPrivilegeName().equals(requestDTO.getPrivilegeName()) &&
            privilegeRepository.existsByPrivilegeName(requestDTO.getPrivilegeName())) {
            throw new RuntimeException("Privilege with name '" + requestDTO.getPrivilegeName() + "' already exists");
        }

        existingPrivilege.setPrivilegeName(requestDTO.getPrivilegeName());
        existingPrivilege.setDescription(requestDTO.getDescription());

        Privilege updatedPrivilege = privilegeRepository.save(existingPrivilege);
        logger.info("Successfully updated privilege with ID: {}", updatedPrivilege.getId());

        return mapToResponseDTO(updatedPrivilege);
    }

    /**
     * Delete privilege by ID
     */
    public void deletePrivilege(Long id) {
        logger.info("Deleting privilege with ID: {}", id);

        if (!privilegeRepository.existsById(id)) {
            throw new RuntimeException("Privilege not found with ID: " + id);
        }

        privilegeRepository.deleteById(id);
        logger.info("Successfully deleted privilege with ID: {}", id);
    }

    /**
     * Search privileges by name
     */
    @Transactional(readOnly = true)
    public List<PrivilegeResponseDTO> searchPrivilegesByName(String searchTerm) {
        logger.info("Searching privileges by name containing: {}", searchTerm);

        List<Privilege> privileges = privilegeRepository.findByPrivilegeNameContainingIgnoreCase(searchTerm);
        return privileges.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Search privileges by description
     */
    @Transactional(readOnly = true)
    public List<PrivilegeResponseDTO> searchPrivilegesByDescription(String searchTerm) {
        logger.info("Searching privileges by description containing: {}", searchTerm);

        List<Privilege> privileges = privilegeRepository.findByDescriptionContainingIgnoreCase(searchTerm);
        return privileges.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Check if privilege exists by name
     */
    @Transactional(readOnly = true)
    public boolean privilegeExists(String privilegeName) {
        return privilegeRepository.existsByPrivilegeName(privilegeName);
    }

    /**
     * Map Privilege entity to PrivilegeResponseDTO
     */
    private PrivilegeResponseDTO mapToResponseDTO(Privilege privilege) {
        return new PrivilegeResponseDTO(
                privilege.getId(),
                privilege.getPrivilegeName(),
                privilege.getDescription()
        );
    }
}
