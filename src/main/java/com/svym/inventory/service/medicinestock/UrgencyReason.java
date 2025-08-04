package com.svym.inventory.service.medicinestock;

public enum UrgencyReason {
	EXPIRED, // at least 1 batch already expired
	OUT_OF_STOCK, // total qty == 0
	CRITICALLY_LOW // 0 < qty < minimumRequired
}
