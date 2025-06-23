package com.svym.inventory.service.distributiontype;

import org.springframework.data.jpa.repository.JpaRepository;

import com.svym.inventory.service.entity.DistributionType;

public interface DistributionTypeRepository extends JpaRepository<DistributionType, Long> {
}
