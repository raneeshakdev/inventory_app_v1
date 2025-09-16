package com.svym.inventory.service.repository;

import com.svym.inventory.service.entity.RolePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, Long> {

    /**
     * Find all privileges for a specific role
     */
    @Query("SELECT rp FROM RolePrivilege rp WHERE rp.roleId = :roleId")
    List<RolePrivilege> findByRoleId(@Param("roleId") Long roleId);

    /**
     * Find all roles that have a specific privilege
     */
    @Query("SELECT rp FROM RolePrivilege rp WHERE rp.privilegeId = :privilegeId")
    List<RolePrivilege> findByPrivilegeId(@Param("privilegeId") Long privilegeId);

    /**
     * Check if a role-privilege association exists
     */
    @Query("SELECT COUNT(rp) > 0 FROM RolePrivilege rp WHERE rp.roleId = :roleId AND rp.privilegeId = :privilegeId")
    boolean existsByRoleIdAndPrivilegeId(@Param("roleId") Long roleId, @Param("privilegeId") Long privilegeId);

    /**
     * Delete a specific role-privilege association
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM RolePrivilege rp WHERE rp.roleId = :roleId AND rp.privilegeId = :privilegeId")
    void deleteByRoleIdAndPrivilegeId(@Param("roleId") Long roleId, @Param("privilegeId") Long privilegeId);

    /**
     * Delete all privileges for a role
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM RolePrivilege rp WHERE rp.roleId = :roleId")
    void deleteAllByRoleId(@Param("roleId") Long roleId);

    /**
     * Get privilege details for a role using JOIN
     */
    @Query("""
        SELECT p.id, p.privilegeName, p.description 
        FROM RolePrivilege rp 
        JOIN Privilege p ON rp.privilegeId = p.id 
        WHERE rp.roleId = :roleId
        ORDER BY p.privilegeName
        """)
    List<Object[]> findPrivilegeDetailsByRoleId(@Param("roleId") Long roleId);
}
