package com.svym.inventory.service.view;

import java.time.LocalDateTime;

public interface MedicineListProjection {
    Long getMedicineId();
    String getMedicineName();
    String getMedicineTypeName();
    Long getLocationId();
    String getLocationName();
    Integer getNumberOfBatches();
    String getStockStatus();
    Boolean getHasExpiredBatches();
    Integer getTotalNumberOfMedicines();
    Integer getNumberOfMedExpired();
    LocalDateTime getUpdatedAt();
    String getUpdatedByFullName();
}
