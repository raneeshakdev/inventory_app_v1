package com.svym.inventory.service.deliverycenter;

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

import com.svym.inventory.service.dto.DeliveryCenterDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/delivery-centers")
@RequiredArgsConstructor
public class DeliveryCenterController {

    private final DeliveryCenterServiceImpl service;

    @GetMapping
    public List<DeliveryCenterDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryCenterDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public DeliveryCenterDTO create(@Valid @RequestBody DeliveryCenterDTO dc) {
        return service.create(dc);
    }

    @PutMapping("/{id}")
    public DeliveryCenterDTO update(@PathVariable Long id, @Valid @RequestBody DeliveryCenterDTO dc) {
        return service.update(id, dc);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
