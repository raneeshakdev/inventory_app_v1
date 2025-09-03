package com.svym.inventory.service.medicinedistribution;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svym.inventory.service.dto.MedicineDistributionDTO;

@RestController
@RequestMapping("/api/v1/medicine-distributions")
public class MedicineDistributionController {

	private final MedicineDistributionService service;

	public MedicineDistributionController(MedicineDistributionService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<MedicineDistributionDTO> create(
			@RequestBody MedicineDistributionDTO dto,
			@RequestHeader(value = "X-Created-By", required = false) String createdBy) {

		// Set createdBy from header if provided
		if (createdBy != null && !createdBy.trim().isEmpty()) {
			dto.setCreatedBy(createdBy.trim());
		}

		return ResponseEntity.ok(service.create(dto));
	}

	@PutMapping("/{id}")
	public ResponseEntity<MedicineDistributionDTO> update(@PathVariable Long id,
			@RequestBody MedicineDistributionDTO dto) {
		return ResponseEntity.ok(service.update(id, dto));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<MedicineDistributionDTO> getById(@PathVariable Long id) {
		return ResponseEntity.ok(service.getById(id));
	}

	@GetMapping
	public ResponseEntity<List<MedicineDistributionDTO>> getAll() {
		return ResponseEntity.ok(service.getAll());
	}
}
