package com.svym.inventory.service.medicinepbatch;

import org.springframework.data.jpa.repository.JpaRepository;

import com.svym.inventory.service.entity.MedicinePurchaseBatch;

public interface MedicinePurchaseBatchRepository extends JpaRepository<MedicinePurchaseBatch, Long> {
}
