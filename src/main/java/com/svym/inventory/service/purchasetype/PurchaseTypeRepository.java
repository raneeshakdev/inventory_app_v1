package com.svym.inventory.service.purchasetype;

import org.springframework.data.jpa.repository.JpaRepository;

import com.svym.inventory.service.entity.PurchaseType;

public interface PurchaseTypeRepository extends JpaRepository<PurchaseType, Long> {
}
