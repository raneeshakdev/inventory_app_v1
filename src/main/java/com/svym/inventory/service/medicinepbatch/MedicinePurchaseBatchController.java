package com.svym.inventory.service.medicinepbatch;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.svym.inventory.service.dto.LocationMedicineStatusDTO;
import com.svym.inventory.service.dto.MedicinePurchaseBatchDTO;
import com.svym.inventory.service.dto.MedicinePurchaseBatchPartialUpdateDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/batches")
@RequiredArgsConstructor
public class MedicinePurchaseBatchController {

	private final MedicinePurchaseBatchService service;

	@PostMapping
	public MedicinePurchaseBatchDTO create(@Valid @RequestBody MedicinePurchaseBatchDTO dto) {
		return service.create(dto);
	}

	@GetMapping("/{id}")
	public MedicinePurchaseBatchDTO getById(@PathVariable Long id) {
		return service.getById(id);
	}

	@GetMapping
	public List<MedicinePurchaseBatchDTO> getAll() {
		return service.getAll();
	}

	@PutMapping("/{id}")
	public MedicinePurchaseBatchDTO update(@PathVariable Long id, @Valid @RequestBody MedicinePurchaseBatchDTO dto) {
		return service.update(id, dto);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}
	

    @GetMapping("/location-wise")
    public ResponseEntity<List<LocationMedicineStatusDTO>> getLocationStatus() {
        return ResponseEntity.ok(service.getLocationWiseStatus());
    }

    @GetMapping("/available")
    public ResponseEntity<List<MedicinePurchaseBatchDTO>> getAvailableBatchesByLocationAndMedicine(
            @RequestParam Long locationId,
            @RequestParam Long medicineId) {
        return ResponseEntity.ok(service.getAvailableBatchesByLocationAndMedicine(locationId, medicineId));
    }

    @GetMapping("/available/location/{locationId}")
    public ResponseEntity<List<MedicinePurchaseBatchDTO>> getAvailableBatchesByLocation(
            @PathVariable Long locationId) {
        return ResponseEntity.ok(service.getAvailableBatchesByLocation(locationId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MedicinePurchaseBatchDTO> partialUpdate(
            @PathVariable Long id,
            @Valid @RequestBody MedicinePurchaseBatchPartialUpdateDTO dto) {
        return ResponseEntity.ok(service.partialUpdate(id, dto));
    }
}
