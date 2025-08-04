package com.svym.inventory.service.medicineditem;

import org.springframework.data.jpa.repository.JpaRepository;

import com.svym.inventory.service.entity.MedicineDistributionItem;

public interface MedicineDistributionItemRepository extends JpaRepository<MedicineDistributionItem, Long> {
}
