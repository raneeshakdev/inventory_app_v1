package com.svym.inventory.service.medicinedistribution;

import java.time.LocalDate;
import java.util.List;

import com.svym.inventory.service.dto.MedicineDistributionDTO;
import com.svym.inventory.service.dto.MedicineDistributionViewDTO;

public interface MedicineDistributionService {
	MedicineDistributionDTO create(MedicineDistributionDTO dto);

	MedicineDistributionDTO update(Long id, MedicineDistributionDTO dto);

	void delete(Long id);

	MedicineDistributionDTO getById(Long id);

	List<MedicineDistributionDTO> getAll();

	List<MedicineDistributionViewDTO> getMedicinesDistributedByPatient(Long deliveryCenterId, LocalDate distributionDate);
}
