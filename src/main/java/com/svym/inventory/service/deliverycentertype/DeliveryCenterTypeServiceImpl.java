package com.svym.inventory.service.deliverycentertype;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.svym.inventory.service.dto.DeliveryCenterTypeDTO;
import com.svym.inventory.service.entity.DeliveryCenterType;
import com.svym.inventory.service.entity.mapper.DeliveryCenterTypeMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryCenterTypeServiceImpl implements DeliveryCenterTypeService {

    private final DeliveryCenterTypeRepository repository;

    private final DeliveryCenterTypeMapper mapper;

    @Override
    public DeliveryCenterTypeDTO create(DeliveryCenterTypeDTO dto) {
        DeliveryCenterType entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public DeliveryCenterTypeDTO getById(Long id) {
        DeliveryCenterType entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("DeliveryCenterType not found"));
        return mapper.toDTO(entity);
    }

    @Override
    public List<DeliveryCenterTypeDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DeliveryCenterTypeDTO update(Long id, DeliveryCenterTypeDTO dto) {
        DeliveryCenterType existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("DeliveryCenterType not found"));
        existing.setTypeName(dto.getTypeName());
        return mapper.toDTO(repository.save(existing));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
