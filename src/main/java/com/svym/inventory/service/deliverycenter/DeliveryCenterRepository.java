package com.svym.inventory.service.deliverycenter;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.svym.inventory.service.entity.DeliveryCenter;

public interface DeliveryCenterRepository extends JpaRepository<DeliveryCenter, Long> {

    // Find delivery centers by location ID
    List<DeliveryCenter> findByLocationId(Long locationId);

    // Find delivery centers by type ID
    List<DeliveryCenter> findByTypeId(Long typeId);

    // Find delivery centers by both location ID and type ID
    List<DeliveryCenter> findByLocationIdAndTypeId(Long locationId, Long typeId);

    // Find active delivery centers by location ID and type ID
    @Query("SELECT dc FROM DeliveryCenter dc WHERE dc.location.id = :locationId AND dc.type.id = :typeId AND dc.isActive = true")
    List<DeliveryCenter> findActiveByLocationIdAndTypeId(@Param("locationId") Long locationId, @Param("typeId") Long typeId);
}
