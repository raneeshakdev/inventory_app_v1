package com.svym.inventory.service.repository;

import com.svym.inventory.service.entity.MedicineActionItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MedicineActionItemsRepository extends JpaRepository<MedicineActionItems, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM MedicineActionItems")
    void deleteAllActionItems();

    @Query("SELECT COUNT(mai) FROM MedicineActionItems mai WHERE mai.location.id = :locationId AND mai.actionType = :actionType")
    Long countByLocationIdAndActionType(@Param("locationId") Long locationId, @Param("actionType") String actionType);

    @Query("SELECT DISTINCT mai.location.id FROM MedicineActionItems mai")
    java.util.List<Long> findDistinctLocationIds();
}
