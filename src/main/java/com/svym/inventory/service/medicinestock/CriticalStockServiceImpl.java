package com.svym.inventory.service.medicinestock;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CriticalStockServiceImpl implements CriticalStockService {

	private final CriticalStockRepository repo;

	public Page<CriticalStockRowDTO> list(String location, String search, int page, int size) {

		Pageable pb = PageRequest.of(page, size, Sort.by("medicineName"));
		return repo.find(location, search, 30, pb); // 30‑day near‑expiry window (not used here, but for symmetry)
	}
}
