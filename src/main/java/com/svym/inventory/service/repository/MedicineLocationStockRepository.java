package com.svym.inventory.service.repository;

import com.svym.inventory.service.entity.MedicineLocationStock;
import com.svym.inventory.service.entity.MedicineLocationStockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineLocationStockRepository extends JpaRepository<MedicineLocationStock, MedicineLocationStockId> {
}
