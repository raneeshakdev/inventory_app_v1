package com.svym.inventory.service.medicinestock;

import org.springframework.data.domain.Page;

public interface MedicineStockService {

	Page<MedicineStockRowDTO> getStockTable(String location, String search, int page, int size);

}
