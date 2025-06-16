package com.svym.inventory.service.controller;

import com.svym.inventory.service.dto.MedicineDto;
import com.svym.inventory.service.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medicines")
@RequiredArgsConstructor
public class MedicineController {
    private final MedicineService medicineService;

    @PostMapping
    public ResponseEntity<MedicineDto> createMedicine(@RequestBody MedicineDto medicineDto) {
        MedicineDto savedMedicine = medicineService.saveDto(medicineDto);
        return new ResponseEntity<>(savedMedicine, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineDto> getMedicineById(@PathVariable Long id) {
        MedicineDto medicine = medicineService.findDtoById(id);
        return ResponseEntity.ok(medicine);
    }

    @GetMapping
    public ResponseEntity<List<MedicineDto>> getAllMedicines() {
        List<MedicineDto> medicines = medicineService.findAllMedicineAsDtoList();
        return ResponseEntity.ok(medicines);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineDto> updateMedicine(@PathVariable Long id, @RequestBody MedicineDto medicineDto) {
        medicineDto.setId(id);
        MedicineDto updatedMedicine = medicineService.saveDto(medicineDto);
        return ResponseEntity.ok(updatedMedicine);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable Long id) {
        medicineService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/low-stock")
    public ResponseEntity<List<MedicineDto>> getLowStockMedicines() {
        List<MedicineDto> medicines = medicineService.findLowStockMedicines().stream()
                .map(medicine -> medicineService.findDtoById(medicine.getId()))
                .toList();
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/available")
    public ResponseEntity<List<MedicineDto>> getAvailableMedicines() {
        List<MedicineDto> medicines = medicineService.findAvailableMedicines().stream()
                .map(medicine -> medicineService.findDtoById(medicine.getId()))
                .toList();
        return ResponseEntity.ok(medicines);
    }
}
