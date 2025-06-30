package com.svym.inventory.service.purchasetype;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.svym.inventory.service.dto.PurchaseTypeDTO;
import com.svym.inventory.service.entity.PurchaseType;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseTypeServiceImpl implements PurchaseTypeService {

	private final PurchaseTypeRepository repository;
	private final ModelMapper mapper;

	@Override
	public PurchaseTypeDTO create(PurchaseTypeDTO dto) {
		PurchaseType entity = mapper.map(dto, PurchaseType.class);
		return mapper.map(repository.save(entity), PurchaseTypeDTO.class);
	}

	@Override
	public PurchaseTypeDTO getById(Long id) {
		if (!repository.existsById(id)) {
			throw new EntityNotFoundException("PurchaseType not found with ID: " + id);
		}
		PurchaseType entity = repository.findById(id).orElseThrow();
		return mapper.map(entity, PurchaseTypeDTO.class);
	}

	@Override
	public List<PurchaseTypeDTO> getAll() {
		return repository.findAll().stream().map(e -> mapper.map(e, PurchaseTypeDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public PurchaseTypeDTO update(Long id, PurchaseTypeDTO dto) {
		if (!repository.existsById(id)) {
			throw new EntityNotFoundException("PurchaseType not found with ID: " + id);
		}
		PurchaseType entity = repository.findById(id).orElseThrow();
		entity.setTypeName(dto.getTypeName());
		entity.setDescription(dto.getDescription());
		return mapper.map(repository.save(entity), PurchaseTypeDTO.class);
	}

	@Override
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new EntityNotFoundException("PurchaseType not found with ID: " + id);
		}
		repository.deleteById(id);
	}
}
