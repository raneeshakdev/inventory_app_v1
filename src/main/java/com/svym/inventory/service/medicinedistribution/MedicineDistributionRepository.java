package com.svym.inventory.service.medicinedistribution;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.svym.inventory.service.entity.MedicineDistribution;

public interface MedicineDistributionRepository extends JpaRepository<MedicineDistribution, Long> {

    @Query("SELECT md FROM MedicineDistribution md WHERE md.patient.id = :patientId AND md.deliveryCenter.id = :deliveryCenterId AND md.distributionDate = :distributionDate")
    Optional<MedicineDistribution> findByPatientIdAndDeliveryCenterIdAndDistributionDate(
        @Param("patientId") Long patientId,
        @Param("deliveryCenterId") Long deliveryCenterId,
        @Param("distributionDate") LocalDate distributionDate
    );
}
