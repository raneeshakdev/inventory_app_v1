package com.svym.inventory.service.privilege;

import com.svym.inventory.service.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    /**
     * Find privilege by privilege name
     */
    Optional<Privilege> findByPrivilegeName(String privilegeName);

    /**
     * Check if privilege exists by privilege name
     */
    boolean existsByPrivilegeName(String privilegeName);

    /**
     * Find privileges by privilege name containing the given text (case-insensitive)
     */
    @Query("SELECT p FROM Privilege p WHERE LOWER(p.privilegeName) LIKE LOWER(CONCAT('%', :privilegeName, '%'))")
    List<Privilege> findByPrivilegeNameContainingIgnoreCase(@Param("privilegeName") String privilegeName);

    /**
     * Find privileges by description containing the given text (case-insensitive)
     */
    @Query("SELECT p FROM Privilege p WHERE LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%'))")
    List<Privilege> findByDescriptionContainingIgnoreCase(@Param("description") String description);

    /**
     * Find all privileges ordered by privilege name
     */
    @Query("SELECT p FROM Privilege p ORDER BY p.privilegeName ASC")
    List<Privilege> findAllOrderByPrivilegeName();
}
