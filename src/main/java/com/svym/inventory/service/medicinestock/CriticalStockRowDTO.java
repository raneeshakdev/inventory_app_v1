package com.svym.inventory.service.medicinestock;

public record CriticalStockRowDTO(Long id, String medicineName, int noOfBatch, int minimumRequired,
		UrgencyReason urgencyReason, String urgencyLabel // “Expired”, “Out of stock”, “Critically Low”
) {

}
