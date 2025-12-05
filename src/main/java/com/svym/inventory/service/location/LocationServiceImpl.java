package com.svym.inventory.service.location;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.svym.inventory.service.dto.LocationDTO;
import com.svym.inventory.service.entity.Location;
import com.svym.inventory.service.entity.UserLocation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

	private final LocationRepository repository;

	private final UserLocationRepository userLocationRepository;

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

		// Update fields individually to avoid ID mapping issues
		location.setName(dto.getName());
		location.setLocationAddress(dto.getLocationAddress());
		location.setImagePath(dto.getImagePath());
		location.setIsActive(dto.getIsActive());
		if (dto.getIsDelete() != null) {
			location.setIsDelete(dto.getIsDelete());
		}

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
		return repository.findAllActiveLocations().stream().map(location -> mapper.map(location, LocationDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new EntityNotFoundException("Location not found with ID: " + id);
		}
		repository.deleteById(id);
	}

	@Override
	public void softDelete(Long id) {
		if (!repository.existsById(id)) {
			throw new EntityNotFoundException("Location not found with ID: " + id);
		}
		Location location = repository.findById(id).orElseThrow();
		location.setIsDelete(true);
		repository.save(location);
	}

	@Override
	public List<LocationDTO> getLocationsByUserId(Long userId) {
		// Check if user has any active locations in user_location table
		long userLocationCount = userLocationRepository.countActiveLocationsByUserId(userId);

		if (userLocationCount > 0) {
			// User has specific locations, return only those
			var userLocations = userLocationRepository.findActiveLocationsByUserId(userId);
			List<Long> locationIds = userLocations.stream()
					.map(UserLocation::getLocationId)
					.collect(Collectors.toList());

			return repository.findAllById(locationIds).stream()
					.filter(location -> !location.getIsDelete())
					.map(location -> mapper.map(location, LocationDTO.class))
					.collect(Collectors.toList());
		} else {
			// No specific locations for user, return all active locations
			return getAll();
		}
	}
}
