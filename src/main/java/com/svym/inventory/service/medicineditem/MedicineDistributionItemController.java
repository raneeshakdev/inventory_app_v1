package com.svym.inventory.service.medicineditem;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svym.inventory.service.dto.MedicineDistributionItemDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/distribution-items")
@RequiredArgsConstructor
public class MedicineDistributionItemController {

	private final MedicineDistributionItemService service;

	@PostMapping
	public MedicineDistributionItemDTO create(@Valid @RequestBody MedicineDistributionItemDTO dto) {
		return service.create(dto);
	}

	@GetMapping("/{id}")
	public MedicineDistributionItemDTO getById(@PathVariable Long id) {
		return service.getById(id);
	}

	@GetMapping
	public List<MedicineDistributionItemDTO> getAll() {
		return service.getAll();
	}

	@PutMapping("/{id}")
	public MedicineDistributionItemDTO update(@PathVariable Long id,
			@Valid @RequestBody MedicineDistributionItemDTO dto) {
		return service.update(id, dto);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}
}
