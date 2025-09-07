package com.svym.inventory.service.medicineditem;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.svym.inventory.service.entity.MedicineDistributionItem;

public interface MedicineDistributionItemRepository extends JpaRepository<MedicineDistributionItem, Long> {

    @Query("SELECT mdi FROM MedicineDistributionItem mdi WHERE mdi.distribution.id = :distributionId AND mdi.medicine.id = :medicineId")
    Optional<MedicineDistributionItem> findByDistributionIdAndMedicineId(
        @Param("distributionId") Long distributionId,
        @Param("medicineId") Long medicineId
    );

    @Query("SELECT mdi FROM MedicineDistributionItem mdi " +
           "WHERE mdi.distribution.patient.id = :patientId " +
           "AND mdi.medicine.id = :medicineId " +
           "AND mdi.distribution.distributionDate = :distributionDate")
    Optional<MedicineDistributionItem> findByPatientIdAndMedicineIdAndDistributionDate(
        @Param("patientId") Long patientId,
        @Param("medicineId") Long medicineId,
        @Param("distributionDate") LocalDate distributionDate
    );
}
