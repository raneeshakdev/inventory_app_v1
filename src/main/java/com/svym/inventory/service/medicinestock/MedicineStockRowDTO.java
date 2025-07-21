package com.svym.inventory.service.medicinestock;

import java.time.LocalDateTime;

public record MedicineStockRowDTO(Long medicineId, String medicineName, String medicineCode, String medicineType,
		int noOfBatches, String updatedBy, LocalDateTime updatedDate, StockStatus stockStatus, String stockLabel,
		ExpiryStatus expiryStatus, String expiryLabel) {

}
