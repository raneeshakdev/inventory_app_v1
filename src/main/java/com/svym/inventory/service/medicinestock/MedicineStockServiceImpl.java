package com.svym.inventory.service.medicinestock;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicineStockServiceImpl implements MedicineStockService {

	private final MedicineStockRepository repo;

	public Page<MedicineStockRowDTO> getStockTable(String location, String search, int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("medicineName"));
		return repo.findStockForLocation(Long.valueOf(location), // ← map UI tab to DB id
				search, 30, // near‑expiry window in days
				pageable);
	}
}
