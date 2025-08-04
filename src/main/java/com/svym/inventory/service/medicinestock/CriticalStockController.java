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
@RequestMapping("/api/v1/critical-stock")
@RequiredArgsConstructor
@CrossOrigin
public class CriticalStockController {
	private final CriticalStockService service;

	    /**
	     * Example call:
	     *   GET /api/critical-stock?location=1&page=0&size=10&search=canna
	     */
	    @GetMapping
	    @Authorization
	    public Page<CriticalStockRowDTO> grid(
	            @RequestParam String location,
	            @RequestParam(required = false) String search,
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size) {

	        return service.list(location, search, page, size);
	    }
}
