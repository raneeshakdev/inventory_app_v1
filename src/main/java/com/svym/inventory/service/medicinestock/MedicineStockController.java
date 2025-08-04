package com.svym.inventory.service.medicinestock;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.svym.inventory.service.custom.annotation.Authorization;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
@CrossOrigin              // ← allow your React / Angular front‑end
public class MedicineStockController {

    private final MedicineStockService service;

    /**
     * Grid data ‑ paged, searchable, filterable by location.
     *
     * Example call:
     *   GET /api/stock?location=1&page=0&size=10&search=de
     */
    @GetMapping
    @Authorization
    public Page<MedicineStockRowDTO> list(
            @RequestParam String location,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return service.getStockTable(location, search, page, size);
    }
}
