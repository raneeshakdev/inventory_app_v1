package com.svym.inventory.service.view;

import java.time.LocalDate;

public interface ExpiringMedicineProjection {
    String getMedicineName();
    String getBatchName();
    Integer getCurrentQuantity();
    LocalDate getExpiryDate();
    Integer getDaysToExpiry();
}
