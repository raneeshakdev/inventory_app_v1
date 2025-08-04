package com.svym.inventory.service.medicinetype;

import org.springframework.data.jpa.repository.JpaRepository;

import com.svym.inventory.service.entity.MedicineType;

public interface MedicineTypeRepository extends JpaRepository<MedicineType, Long> {
}
