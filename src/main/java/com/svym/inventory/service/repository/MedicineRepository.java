package com.svym.inventory.service.repository;

import com.svym.inventory.service.entity.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByStockStatusFalse();

    @Query("SELECT m FROM Medicine m WHERE m.isActive = true AND m.currentBatchesCount > 0")
    List<Medicine> findAvailableMedicines();

    // Override to ensure pagination support is explicit
    Page<Medicine> findAll(Pageable pageable);
}
