package com.svym.inventory.service.medicinedistribution;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.svym.inventory.service.deliverycenter.DeliveryCenterRepository;
import com.svym.inventory.service.distributiontype.DistributionTypeRepository;
import com.svym.inventory.service.dto.MedicineDistributionDTO;
import com.svym.inventory.service.entity.MedicineDistribution;
import com.svym.inventory.service.entity.mapper.MedicineDistributionMapper;
import com.svym.inventory.service.patientdetail.PatientDetailRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicineDistributionServiceImpl implements MedicineDistributionService {

	private final MedicineDistributionRepository repository;
	private final MedicineDistributionMapper mapper;
	private final PatientDetailRepository patientDetailRepository;
	private final DistributionTypeRepository distributionTypeRepository;
	private final DeliveryCenterRepository deliveryCenterRepository;

	@Override
	public MedicineDistributionDTO create(MedicineDistributionDTO dto) {
		MedicineDistribution entity = mapper.toEntity(dto);
		entity.setPatient(patientDetailRepository.findById(dto.getPatientId())
				.orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + dto.getPatientId())));
		entity.setDistributionType(distributionTypeRepository.findById(dto.getDistributionTypeId())
				.orElseThrow(() -> new EntityNotFoundException(
						"DistributionType not found with ID: " + dto.getDistributionTypeId())));
		entity.setDeliveryCenter(deliveryCenterRepository.findById(dto.getDeliveryCenterId()).orElseThrow(
				() -> new EntityNotFoundException("DeliveryCenter not found with ID: " + dto.getDeliveryCenterId())));
		return mapper.toDTO(repository.save(entity));
	}

	@Override
	public MedicineDistributionDTO update(Long id, MedicineDistributionDTO dto) {
		MedicineDistribution entity = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("MedicineDistribution not found with ID: " + id));
		entity.setPatient(patientDetailRepository.findById(dto.getPatientId())
				.orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + dto.getPatientId())));
		entity.setDistributionType(distributionTypeRepository.findById(dto.getDistributionTypeId())
				.orElseThrow(() -> new EntityNotFoundException(
						"DistributionType not found with ID: " + dto.getDistributionTypeId())));
		entity.setDeliveryCenter(deliveryCenterRepository.findById(dto.getDeliveryCenterId()).orElseThrow(
				() -> new EntityNotFoundException("DeliveryCenter not found with ID: " + dto.getDeliveryCenterId())));
		mapper.updateEntityFromDto(dto, entity);
		return mapper.toDTO(repository.save(entity));
	}

	@Override
	public void delete(Long id) {
		MedicineDistribution entity = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("MedicineDistribution not found with ID: " + id));
		repository.delete(entity);
	}

	@Override
	public MedicineDistributionDTO getById(Long id) {
		return repository.findById(id).map(mapper::toDTO)
				.orElseThrow(() -> new EntityNotFoundException("MedicineDistribution not found with ID: " + id));
	}

	@Override
	public List<MedicineDistributionDTO> getAll() {
		return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
	}
}
