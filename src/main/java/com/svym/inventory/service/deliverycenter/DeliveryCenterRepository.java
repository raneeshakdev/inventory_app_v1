package com.svym.inventory.service.deliverycenter;

import org.springframework.data.jpa.repository.JpaRepository;

import com.svym.inventory.service.entity.DeliveryCenter;

public interface DeliveryCenterRepository extends JpaRepository<DeliveryCenter, Long> {
}
