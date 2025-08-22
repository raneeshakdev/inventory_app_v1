package com.svym.inventory.service.medicinepbatch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.svym.inventory.service.dto.LocationMedicineStatusDTO;
import com.svym.inventory.service.entity.MedicinePurchaseBatch;

@Repository
public interface MedicinePurchaseBatchRepository extends JpaRepository<MedicinePurchaseBatch, Long> {

	@Query("""
			    SELECT new com.svym.inventory.service.dto.LocationMedicineStatusDTO(
			        b.location,
			        SUM(CASE WHEN b.currentQuantity = 0 THEN 1 ELSE 0 END),
			        SUM(CASE WHEN b.expiryDate < CURRENT_DATE THEN 1 ELSE 0 END),
			        SUM(CASE WHEN b.expiryDate BETWEEN CURRENT_DATE AND :cutoff THEN 1 ELSE 0 END)
			    )
			    FROM MedicinePurchaseBatch b
			    GROUP BY b.location
			""")
	List<LocationMedicineStatusDTO> fetchLocationWiseStatus(@Param("cutoff") LocalDate cutoff);

	@Query("""
			SELECT b FROM MedicinePurchaseBatch b 
			WHERE b.location.id = :locationId 
			AND b.medicine.id = :medicineId 
			AND b.isActive = true 
			AND b.currentQuantity > 0
			ORDER BY b.expiryDate ASC
			""")
	List<MedicinePurchaseBatch> findAvailableBatchesByLocationAndMedicine(
		@Param("locationId") Long locationId,
		@Param("medicineId") Long medicineId);

	@Query("""
			SELECT b FROM MedicinePurchaseBatch b 
			WHERE b.location.id = :locationId 
			AND b.isActive = true 
			AND b.currentQuantity > 0
			ORDER BY b.medicine.medicineName ASC, b.expiryDate ASC
			""")
	List<MedicinePurchaseBatch> findAvailableBatchesByLocation(@Param("locationId") Long locationId);

	@Modifying
	@Transactional
	@Query("""
			UPDATE MedicinePurchaseBatch b 
			SET b.batchName = :batchName, 
			    b.expiryDate = :expiryDate,
			    b.lastModifiedBy = :lastModifiedBy,
			    b.lastUpdatedAt = :lastUpdatedAt
			WHERE b.id = :id
			""")
	int updateBatchNameAndExpiryDate(
		@Param("id") Long id,
		@Param("batchName") String batchName,
		@Param("expiryDate") LocalDateTime expiryDate,
		@Param("lastModifiedBy") String lastModifiedBy,
		@Param("lastUpdatedAt") LocalDateTime lastUpdatedAt);
}
