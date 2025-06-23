package com.svym.inventory.service.entity.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.svym.inventory.service.dto.EventDetailDTO;
import com.svym.inventory.service.entity.EventDetail;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventDetailMapper {

	private final ModelMapper modelMapper = new ModelMapper();

	public EventDetail toEntity(EventDetailDTO dto) {
		return modelMapper.map(dto, EventDetail.class);
	}

	public EventDetailDTO toDto(EventDetail entity) {
		return modelMapper.map(entity, EventDetailDTO.class);
	}
}
