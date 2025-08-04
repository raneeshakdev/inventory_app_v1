package com.svym.inventory.service.view.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.svym.inventory.service.entity.MedicinePurchaseBatch;
import com.svym.inventory.service.view.ExpiringMedicineProjection;

@Repository
public interface ExpiringMedicineRepository extends JpaRepository<MedicinePurchaseBatch, Long> {

    @Query(value = "SELECT m.name AS medicine_name, mpb.batch_name, mpb.current_quantity, " +
            "mpb.expiry_date, mpb.days_to_expiry " +
            "FROM v_expiring_medicines", nativeQuery = true)
    List<Object[]> findExpiringMedicinesRaw();

    // Optional: Project directly to DTO using Spring Projection
    @Query(value = "SELECT m.name AS medicine_name, l.name AS loation_name, mpb.batch_name, mpb.current_quantity, " +
            "mpb.expiry_date, mpb.days_to_expiry " +
            "FROM v_expiring_medicines", nativeQuery = true)
    List<ExpiringMedicineProjection> findExpiringMedicines();
}
