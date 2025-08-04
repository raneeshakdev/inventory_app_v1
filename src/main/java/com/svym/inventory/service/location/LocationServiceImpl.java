package com.svym.inventory.service.location;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.svym.inventory.service.dto.LocationDTO;
import com.svym.inventory.service.entity.Location;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

	private final LocationRepository repository;

	private final ModelMapper mapper;

	@Override
	public LocationDTO create(LocationDTO dto) {
		Location location = mapper.map(dto, Location.class);
		return mapper.map(repository.save(location), LocationDTO.class);
	}

	@Override
	public LocationDTO update(Long id, LocationDTO dto) {
		if (!repository.existsById(id)) {
			throw new EntityNotFoundException("Location not found with ID: " + id);
		}
		Location location = repository.findById(id).orElseThrow();
		mapper.map(dto, location);
		return mapper.map(repository.save(location), LocationDTO.class);
	}

	@Override
	public LocationDTO getById(Long id) {
		if (!repository.existsById(id)) {
			throw new EntityNotFoundException("Location not found with ID: " + id);
		}
		Location location = repository.findById(id).orElseThrow();
		return mapper.map(location, LocationDTO.class);
	}

	@Override
	public List<LocationDTO> getAll() {
		return repository.findAll().stream().map(location -> mapper.map(location, LocationDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new EntityNotFoundException("Location not found with ID: " + id);
		}
		repository.deleteById(id);
	}
}
