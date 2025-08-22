package com.svym.inventory.service.repository;

import com.svym.inventory.service.entity.Medicine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends CrudRepository<Medicine, Long> {
    List<Medicine> findByStockStatusFalse();

    @Query("SELECT m FROM Medicine m WHERE m.isActive = true AND m.currentBatchesCount > 0")
    List<Medicine> findAvailableMedicines();
}
