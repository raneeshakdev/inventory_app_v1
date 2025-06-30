package com.svym.inventory.service.distributiontype;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.svym.inventory.service.dto.DistributionTypeDTO;
import com.svym.inventory.service.entity.DistributionType;
import com.svym.inventory.service.entity.mapper.DistributionTypeMapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DistributionTypeServiceImpl implements DistributionTypeService {

    private final DistributionTypeRepository repository;
    private final DistributionTypeMapper mapper;

    @Override
    public DistributionTypeDTO create(DistributionTypeDTO dto) {
        DistributionType entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public DistributionTypeDTO update(Long id, DistributionTypeDTO dto) {
        DistributionType existing = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Not found with ID: " + id));
        existing.setTypeName(dto.getTypeName());
        existing.setDescription(dto.getDescription());
        return mapper.toDto(repository.save(existing));
    }

    @Override
    public DistributionTypeDTO getById(Long id) {
        return repository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException("Not found with ID: " + id));
    }

    @Override
    public List<DistributionTypeDTO> getAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
