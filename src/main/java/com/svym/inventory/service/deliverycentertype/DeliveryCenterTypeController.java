package com.svym.inventory.service.deliverycentertype;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svym.inventory.service.dto.DeliveryCenterTypeDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/delivery-center-types")
@RequiredArgsConstructor
public class DeliveryCenterTypeController {

	private final DeliveryCenterTypeService service;

	@GetMapping
	public List<DeliveryCenterTypeDTO> getAll() {
		return service.getAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<DeliveryCenterTypeDTO> getById(@PathVariable Long id) {
		return ResponseEntity.ok(service.getById(id));
	}

	@PostMapping
	public ResponseEntity<DeliveryCenterTypeDTO> create(@Valid @RequestBody DeliveryCenterTypeDTO dto) {
		return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<DeliveryCenterTypeDTO> update(@PathVariable Long id,
			@Valid @RequestBody DeliveryCenterTypeDTO dto) {
		return ResponseEntity.ok(service.update(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
