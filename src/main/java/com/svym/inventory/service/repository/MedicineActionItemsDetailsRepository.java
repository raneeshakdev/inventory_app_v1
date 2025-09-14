package com.svym.inventory.service.repository;

import com.svym.inventory.service.entity.MedicineActionItemsDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for MedicineActionItemsDetails view.
 * Provides read-only access to the medicine_action_items_details view.
 */
@Repository
public interface MedicineActionItemsDetailsRepository extends JpaRepository<MedicineActionItemsDetails, Long> {

    /**
     * Find all action items for a specific location.
     * @param locationId the location ID to filter by
     * @return list of medicine action items details for the location
     */
    @Query("SELECT m FROM MedicineActionItemsDetails m WHERE m.locationId = :locationId")
    List<MedicineActionItemsDetails> findAllByLocationId(@Param("locationId") Long locationId);
}
