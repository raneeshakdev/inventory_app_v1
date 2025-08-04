package com.svym.inventory.service.medicinepbatch;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
