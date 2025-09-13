package com.svym.inventory.service.repository;

import com.svym.inventory.service.entity.MedicineActionItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MedicineActionItemsRepository extends JpaRepository<MedicineActionItems, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM MedicineActionItems")
    void deleteAllActionItems();
}
