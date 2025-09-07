package com.svym.inventory.service.repository;

import com.svym.inventory.service.entity.MedicineDistributionView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicineDistributionViewRepository extends JpaRepository<MedicineDistributionView, MedicineDistributionView.MedicineDistributionViewId> {

    @Query("SELECT mdv FROM MedicineDistributionView mdv " +
           "WHERE mdv.deliveryCenterId = :deliveryCenterId " +
           "AND mdv.distributionDate = :distributionDate " +
           "ORDER BY mdv.patientName, mdv.medicineName")
    List<MedicineDistributionView> findByDeliveryCenterIdAndDistributionDate(
        @Param("deliveryCenterId") Long deliveryCenterId,
        @Param("distributionDate") LocalDate distributionDate
    );
}
