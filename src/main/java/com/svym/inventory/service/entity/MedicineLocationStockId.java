package com.svym.inventory.service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineLocationStockId implements Serializable {
    private Long medicineId;
    private Long locationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicineLocationStockId that = (MedicineLocationStockId) o;
        return Objects.equals(medicineId, that.medicineId) &&
               Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicineId, locationId);
    }
}
