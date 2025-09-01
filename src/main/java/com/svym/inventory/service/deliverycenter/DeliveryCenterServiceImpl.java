package com.svym.inventory.service.deliverycenter;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.svym.inventory.service.deliverycentertype.DeliveryCenterTypeRepository;
import com.svym.inventory.service.dto.DeliveryCenterDTO;
import com.svym.inventory.service.entity.DeliveryCenter;
import com.svym.inventory.service.location.LocationRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryCenterServiceImpl implements DeliveryCenterService {

	private final DeliveryCenterRepository repository;
	private final ModelMapper modelMapper;
	private final LocationRepository locationRepository;
	private final DeliveryCenterTypeRepository typeRepository;
	
	private static final String DELIVERY_CENTER_TYPE_NOT_FOUND = "DeliveryCenterType not found with ID: ";

	public List<DeliveryCenterDTO> getAll() {
		return repository.findAll().stream()
				.map(dc -> modelMapper.map(dc, DeliveryCenterDTO.class))
				.collect(Collectors.toList());
	}

	public DeliveryCenterDTO getById(Long id) {
        DeliveryCenter entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DeliveryCenter not found with ID:"+ id));
        return modelMapper.map(entity, DeliveryCenterDTO.class);
	}

	public DeliveryCenterDTO create(@Valid DeliveryCenterDTO dc) {
		DeliveryCenter deliveryCenter = modelMapper.map(dc, DeliveryCenter.class);
		deliveryCenter.setType(typeRepository.findById(dc.getTypeId())
				.orElseThrow(() -> new EntityNotFoundException(DELIVERY_CENTER_TYPE_NOT_FOUND + dc.getTypeId())));
		deliveryCenter.setLocation(locationRepository.findById(dc.getLocationId())
				.orElseThrow(() -> new EntityNotFoundException("Location not found with ID: " + dc.getLocationId())));
		deliveryCenter.setCreatedAt(java.time.LocalDateTime.now());
		deliveryCenter.setUpdatedAt(java.time.LocalDateTime.now());
		return modelMapper.map(repository.save(deliveryCenter), DeliveryCenterDTO.class);
	}

	public DeliveryCenterDTO update(Long id, @Valid DeliveryCenterDTO dc) {
        DeliveryCenter existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DeliveryCenter not found with ID: " + id));
        existing.setName(dc.getName());
        existing.setContactPhone(dc.getContactPhone());
        existing.setType(typeRepository.findById(dc.getTypeId())
				.orElseThrow(() -> new EntityNotFoundException(DELIVERY_CENTER_TYPE_NOT_FOUND + dc.getTypeId())));
        existing.setLocation(locationRepository.findById(dc.getLocationId())
				.orElseThrow(() -> new EntityNotFoundException("Location not found with ID: " + dc.getLocationId())));
        existing.setIsActive(dc.getIsActive());
        existing.setUpdatedAt(java.time.LocalDateTime.now());
        return modelMapper.map(repository.save(existing), DeliveryCenterDTO.class);
	}

	public void delete(Long id) {
			DeliveryCenter entity = repository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("DeliveryCenter not found with ID: " + id));
	repository.delete(entity);
	}

	@Override
	public List<DeliveryCenterDTO> getByLocationAndType(Long locationId, Long typeId) {
		// Validate that both parameters are mandatory
		if (locationId == null) {
			throw new IllegalArgumentException("Location ID is mandatory and cannot be null");
		}
		if (typeId == null) {
			throw new IllegalArgumentException("Type ID is mandatory and cannot be null");
		}

		// Since both parameters are mandatory, we always use the combined query
		List<DeliveryCenter> deliveryCenters = repository.findActiveByLocationIdAndTypeId(locationId, typeId);

		return deliveryCenters.stream()
				.map(dc -> modelMapper.map(dc, DeliveryCenterDTO.class))
				.collect(Collectors.toList());
	}
}
