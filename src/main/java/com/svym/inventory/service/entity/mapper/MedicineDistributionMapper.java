package com.svym.inventory.service.entity.mapper;

import org.springframework.stereotype.Component;

import com.svym.inventory.service.dto.MedicineDistributionDTO;
import com.svym.inventory.service.entity.MedicineDistribution;
import com.svym.inventory.service.security.UserUtils;

@Component
public class MedicineDistributionMapper {

	public MedicineDistributionDTO toDTO(MedicineDistribution entity) {
		MedicineDistributionDTO dto = new MedicineDistributionDTO();
		dto.setId(entity.getId());
		dto.setDistributionItems(entity.getDistributionItems());
		dto.setCreatedBy(UserUtils.getCurrentUser());
		dto.setNotes(entity.getNotes());
		dto.setTotalItems(entity.getTotalItems());
		dto.setPatientId(entity.getPatient() != null ? entity.getPatient().getId() : null);
		dto.setDistributionTypeId(entity.getDistributionType() != null ? entity.getDistributionType().getId() : null);
		dto.setDeliveryCenterId(entity.getDeliveryCenter() != null ? entity.getDeliveryCenter().getId() : null);
		return dto;
	}

	public MedicineDistribution toEntity(MedicineDistributionDTO dto) {
		MedicineDistribution entity = new MedicineDistribution();
		entity.setId(dto.getId());
		entity.setDistributionItems(dto.getDistributionItems());
		entity.setCreatedBy(UserUtils.getCurrentUser());
		entity.setNotes(dto.getNotes());
		entity.setTotalItems(dto.getTotalItems());
		return entity;
	}

	public void updateEntityFromDto(MedicineDistributionDTO dto, MedicineDistribution entity) {
		entity.setDistributionItems(dto.getDistributionItems());
		entity.setNotes(dto.getNotes());
		entity.setTotalItems(dto.getTotalItems());
	}
}
