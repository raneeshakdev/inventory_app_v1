package com.svym.inventory.service.entity.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.svym.inventory.service.dto.DistributionTypeDTO;
import com.svym.inventory.service.entity.DistributionType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DistributionTypeMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public DistributionType toEntity(DistributionTypeDTO dto) {
        return modelMapper.map(dto, DistributionType.class);
    }

    public DistributionTypeDTO toDto(DistributionType entity) {
        return modelMapper.map(entity, DistributionTypeDTO.class);
    }
}
