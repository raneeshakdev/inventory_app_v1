package com.svym.inventory.service.deliverycenter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.svym.inventory.service.dto.DeliveryCenterDTO;
import com.svym.inventory.service.entity.DeliveryCenter;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryCenterService {

	@Autowired
	private DeliveryCenterRepository repository;

	public List<DeliveryCenter> getAll() {
		return repository.findAll();
	}

	public Optional<DeliveryCenter> getById(Long id) {
		return repository.findById(id);
	}

	public DeliveryCenterDTO create(@Valid DeliveryCenterDTO dc) {
		ModelMapper mapper = new ModelMapper();
		DeliveryCenter deliveryCenter = mapper.map(dc, DeliveryCenter.class);
		return mapper.map(repository.save(deliveryCenter), DeliveryCenterDTO.class);
	}

	public DeliveryCenterDTO update(Long id, @Valid DeliveryCenterDTO dc) {
		ModelMapper mapper = new ModelMapper();
		dc.setId(id);
		DeliveryCenter deliveryCenter = mapper.map(dc, DeliveryCenter.class);
		return mapper.map(repository.save(deliveryCenter), DeliveryCenterDTO.class);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}
}
