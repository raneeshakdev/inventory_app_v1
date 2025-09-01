package com.svym.inventory.service.patientdetail;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.svym.inventory.service.dto.PatientDetailDTO;
import com.svym.inventory.service.entity.PatientDetail;
import com.svym.inventory.service.exception.DuplicatePatientIdException;
import com.svym.inventory.service.exception.DuplicatePhoneNumberException;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientDetailServiceImpl implements PatientDetailService {

	private final PatientDetailRepository repository;

	private final ModelMapper modelMapper;

	@Override
	public PatientDetailDTO create(PatientDetailDTO dto) {
		// Check if patientId already exists
		if (dto.getPatientId() != null && repository.existsByPatientId(dto.getPatientId())) {
			throw new DuplicatePatientIdException("Patient ID '" + dto.getPatientId() + "' already exists");
		}

		// Check if phone number already exists
		if (dto.getPhone() != null && repository.existsByPhone(dto.getPhone())) {
			throw new DuplicatePhoneNumberException("Phone number '" + dto.getPhone() + "' already exists");
		}

		PatientDetail entity = modelMapper.map(dto, PatientDetail.class);
		return modelMapper.map(repository.save(entity), PatientDetailDTO.class);
	}

	@Override
	public List<PatientDetailDTO> getAll() {
		return repository.findAll().stream().map(entity -> modelMapper.map(entity, PatientDetailDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public PatientDetailDTO getById(Long id) {
		if (!repository.existsById(id)) {
			throw new EntityNotFoundException("PatientDetail not found with ID: " + id);
		}
		PatientDetail entity = repository.findById(id).orElseThrow();
		return modelMapper.map(entity, PatientDetailDTO.class);
	}

	@Override
	public PatientDetailDTO update(Long id, PatientDetailDTO dto) {
		if (!repository.existsById(id)) {
			throw new EntityNotFoundException("PatientDetail not found with ID: " + id);
		}
		PatientDetail entity = repository.findById(id).orElseThrow();
		modelMapper.map(dto, entity);
		entity.setId(id);
		return modelMapper.map(repository.save(entity), PatientDetailDTO.class);
	}

	@Override
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new EntityNotFoundException("PatientDetail not found with ID: " + id);
		}
		repository.deleteById(id);
	}

	@Override
	public List<PatientDetailDTO> searchPatients(String searchTerm) {
		if (searchTerm == null || searchTerm.trim().isEmpty()) {
			return getAll();
		}
		return repository.findByNameOrPatientIdOrPhoneContainingIgnoreCase(searchTerm.trim())
				.stream()
				.map(entity -> modelMapper.map(entity, PatientDetailDTO.class))
				.collect(Collectors.toList());
	}
}
