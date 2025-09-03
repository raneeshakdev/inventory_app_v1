package com.svym.inventory.service.view.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.svym.inventory.service.entity.MedicinePurchaseBatch;
import com.svym.inventory.service.view.MedicineLocationProjection;

@Repository
public interface MedicineLocationRepository extends JpaRepository<MedicinePurchaseBatch, Long> {

    @Query(value = "SELECT medicine_name, number_of_batches, location_id, medicine_id, total_number_of_medicines " +
            "FROM medicine_location_view", nativeQuery = true)
    List<Object[]> findAllMedicineLocationsRaw();

    @Query(value = "SELECT medicine_name as medicineName, number_of_batches as numberOfBatches, " +
            "location_id as locationId, medicine_id as medicineId, total_number_of_medicines as totalNumberOfMedicines " +
            "FROM medicine_location_view", nativeQuery = true)
    List<MedicineLocationProjection> findAllMedicineLocations();

    @Query(value = "SELECT medicine_name as medicineName, number_of_batches as numberOfBatches, " +
            "location_id as locationId, medicine_id as medicineId, total_number_of_medicines as totalNumberOfMedicines " +
            "FROM medicine_location_view WHERE location_id = :locationId", nativeQuery = true)
    List<MedicineLocationProjection> findByLocationId(@Param("locationId") Long locationId);

    @Query(value = "SELECT medicine_name as medicineName, number_of_batches as numberOfBatches, " +
            "location_id as locationId, medicine_id as medicineId, total_number_of_medicines as totalNumberOfMedicines " +
            "FROM medicine_location_view WHERE location_id = :locationId AND total_number_of_medicines > 0", nativeQuery = true)
    List<MedicineLocationProjection> findByLocationIdWithAvailableMedicines(@Param("locationId") Long locationId);

    @Query(value = "SELECT medicine_name as medicineName, number_of_batches as numberOfBatches, " +
            "location_id as locationId, medicine_id as medicineId, total_number_of_medicines as totalNumberOfMedicines " +
            "FROM medicine_location_view WHERE location_id = :locationId AND total_number_of_medicines > 0 " +
            "AND LOWER(medicine_name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))", nativeQuery = true)
    List<MedicineLocationProjection> searchAvailableMedicinesByLocationIdAndName(
            @Param("locationId") Long locationId,
            @Param("searchTerm") String searchTerm);

    @Query(value = "SELECT medicine_name as medicineName, number_of_batches as numberOfBatches, " +
            "location_id as locationId, medicine_id as medicineId, total_number_of_medicines as totalNumberOfMedicines " +
            "FROM medicine_location_view WHERE location_id = :locationId " +
            "AND LOWER(medicine_name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))", nativeQuery = true)
    List<MedicineLocationProjection> searchMedicinesByLocationIdAndName(
            @Param("locationId") Long locationId,
            @Param("searchTerm") String searchTerm);

    @Query(value = "SELECT medicine_name as medicineName, number_of_batches as numberOfBatches, " +
            "location_id as locationId, medicine_id as medicineId, total_number_of_medicines as totalNumberOfMedicines " +
            "FROM medicine_location_view WHERE total_number_of_medicines > 0 " +
            "AND LOWER(medicine_name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))", nativeQuery = true)
    List<MedicineLocationProjection> searchAvailableMedicinesByName(@Param("searchTerm") String searchTerm);
}
