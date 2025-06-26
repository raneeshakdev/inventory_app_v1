package com.svym.inventory.service.view.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.svym.inventory.service.view.ExpiringMedicineDTO;
import com.svym.inventory.service.view.service.ExpiringMedicineService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/medicines")
@RequiredArgsConstructor
public class ExpiringMedicineController {

    private final ExpiringMedicineService service;

    @GetMapping("/expiring")
    public ResponseEntity<List<ExpiringMedicineDTO>> getExpiringMedicines() {
        return ResponseEntity.ok(service.getExpiringMedicines());
    }
}
