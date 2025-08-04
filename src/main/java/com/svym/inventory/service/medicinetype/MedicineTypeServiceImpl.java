package com.svym.inventory.service.medicinetype;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.svym.inventory.service.dto.MedicineTypeDTO;
import com.svym.inventory.service.entity.MedicineType;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicineTypeServiceImpl implements MedicineTypeService {

	private final MedicineTypeRepository repository;
	private final ModelMapper mapper;

	@Override
	public MedicineTypeDTO create(MedicineTypeDTO dto) {
		MedicineType entity = mapper.map(dto, MedicineType.class);
		return mapper.map(repository.save(entity), MedicineTypeDTO.class);
	}

	@Override
	public MedicineTypeDTO getById(Long id) {
		if (!repository.existsById(id)) {
			throw new EntityNotFoundException("MedicineType not found with ID: " + id);
		}
		MedicineType entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
		return mapper.map(entity, MedicineTypeDTO.class);
	}

	@Override
	public List<MedicineTypeDTO> getAll() {
		return repository.findAll().stream().map(entity -> mapper.map(entity, MedicineTypeDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public MedicineTypeDTO update(Long id, MedicineTypeDTO dto) {
		MedicineType entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found"));
		mapper.map(dto, entity);
		return mapper.map(repository.save(entity), MedicineTypeDTO.class);
	}

	@Override
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new EntityNotFoundException("MedicineType not found with ID: " + id);
		}
		repository.deleteById(id);
	}
}
