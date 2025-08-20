package com.svym.inventory.service.view;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class MedicineListDTO {
    private Long medicineId;
    private String medicineName;
    private String medicineTypeName;
    private Long locationId;
    private String locationName;
    private Integer numberOfBatches;
    private String stockStatus;
    private Boolean hasExpiredBatches;
    private Integer totalNumberOfMedicines;
    private Integer numberOfMedExpired;
}
