package com.svym.inventory.service.medicinedistribution;
import org.springframework.data.jpa.repository.JpaRepository;

import com.svym.inventory.service.entity.MedicineDistribution;

public interface MedicineDistributionRepository extends JpaRepository<MedicineDistribution, Long> {
}
