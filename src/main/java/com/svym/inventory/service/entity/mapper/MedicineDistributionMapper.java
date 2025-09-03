package com.svym.inventory.service.entity.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.svym.inventory.service.dto.MedicineDistributionDTO;
import com.svym.inventory.service.entity.MedicineDistribution;
import com.svym.inventory.service.security.UserUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MedicineDistributionMapper {

	private final MedicineDistributionItemMapper itemMapper;

	public MedicineDistributionDTO toDTO(MedicineDistribution entity) {
		MedicineDistributionDTO dto = new MedicineDistributionDTO();
		dto.setId(entity.getId());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setDistributionDate(entity.getDistributionDate());
		dto.setPatientId(entity.getPatient() != null ? entity.getPatient().getId() : null);
		dto.setDeliveryCenterId(entity.getDeliveryCenter() != null ? entity.getDeliveryCenter().getId() : null);

		// Map distribution items
		if (entity.getDistributionItems() != null) {
			dto.setDistributionItems(entity.getDistributionItems().stream()
					.map(itemMapper::toDTO)
					.collect(Collectors.toList()));
		}

		return dto;
	}

	public MedicineDistribution toEntity(MedicineDistributionDTO dto) {
		MedicineDistribution entity = new MedicineDistribution();
		entity.setId(dto.getId());
		entity.setCreatedBy(dto.getCreatedBy() != null ? dto.getCreatedBy() : UserUtils.getCurrentUser().getId().toString());
		entity.setDistributionDate(dto.getDistributionDate());
		return entity;
	}

	public void updateEntityFromDto(MedicineDistributionDTO dto, MedicineDistribution entity) {
		entity.setDistributionDate(dto.getDistributionDate());
		if (dto.getCreatedBy() != null) {
			entity.setCreatedBy(dto.getCreatedBy());
		}
	}
}
