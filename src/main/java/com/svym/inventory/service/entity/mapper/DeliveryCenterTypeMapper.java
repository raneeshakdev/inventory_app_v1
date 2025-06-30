package com.svym.inventory.service.entity.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.svym.inventory.service.dto.DeliveryCenterTypeDTO;
import com.svym.inventory.service.entity.DeliveryCenterType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeliveryCenterTypeMapper {

	private final ModelMapper modelMapper;

	public DeliveryCenterTypeDTO toDTO(DeliveryCenterType entity) {
		return modelMapper.map(entity, DeliveryCenterTypeDTO.class);
	}

	public DeliveryCenterType toEntity(DeliveryCenterTypeDTO dto) {
		return modelMapper.map(dto, DeliveryCenterType.class);
	}

	public DeliveryCenterType updateEntityFromDto(DeliveryCenterTypeDTO dto, DeliveryCenterType existingEntity) {
		if (dto == null || existingEntity == null) {
			return existingEntity;
		}
		modelMapper.map(dto, existingEntity);
		return existingEntity;
	}
}
