package com.svym.inventory.service.distributiontype;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svym.inventory.service.dto.DistributionTypeDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/distribution-types")
public class DistributionTypeController {

    private final DistributionTypeService service;

    public DistributionTypeController(DistributionTypeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DistributionTypeDTO> create(@Valid @RequestBody DistributionTypeDTO dto) {
        return ResponseEntity.status(201).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DistributionTypeDTO> update(@PathVariable Long id, @Valid @RequestBody DistributionTypeDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DistributionTypeDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<DistributionTypeDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
