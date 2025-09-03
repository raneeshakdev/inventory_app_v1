package com.svym.inventory.service.view;

public interface MedicineLocationProjection {
    String getMedicineName();
    Integer getNumberOfBatches();
    Long getLocationId();
    Long getMedicineId();
    Integer getTotalNumberOfMedicines();
}
