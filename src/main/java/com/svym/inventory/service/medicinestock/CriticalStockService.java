package com.svym.inventory.service.medicinestock;

import org.springframework.data.domain.Page;

public interface CriticalStockService {

	Page<CriticalStockRowDTO> list(String location, String search, int page, int size);

}
