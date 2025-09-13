package com.svym.inventory.service.repository;

import com.svym.inventory.service.entity.MedicineLocationStock;
import com.svym.inventory.service.entity.MedicineLocationStockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineLocationStockRepository extends JpaRepository<MedicineLocationStock, MedicineLocationStockId> {
    @Query("SELECT mls FROM MedicineLocationStock mls WHERE mls.medicineId = :medicineId")
    List<MedicineLocationStock> findByMedicineId(@Param("medicineId") Long medicineId);
}
