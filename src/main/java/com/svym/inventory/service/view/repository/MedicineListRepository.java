package com.svym.inventory.service.view.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.svym.inventory.service.entity.Medicine;
import com.svym.inventory.service.view.MedicineListProjection;

@Repository
public interface MedicineListRepository extends JpaRepository<Medicine, Long> {

    @Query(value = "SELECT medicine_id, medicine_name, medicine_type_name, " +
            "location_id, location_name, number_of_batches, stock_status, " +
            "has_expired_batches, total_number_of_medicines, number_of_med_expired, " +
            "updated_at, updated_by_full_name " +
            "FROM v_medicine_list WHERE location_id = :locationId", nativeQuery = true)
    List<MedicineListProjection> findMedicinesByLocationId(@Param("locationId") Long locationId);

    @Query(value = "SELECT medicine_id, medicine_name, medicine_type_name, " +
            "location_id, location_name, number_of_batches, stock_status, " +
            "has_expired_batches, total_number_of_medicines, number_of_med_expired, " +
            "updated_at, updated_by_full_name " +
            "FROM v_medicine_list", nativeQuery = true)
    List<MedicineListProjection> findAllMedicines();
}
