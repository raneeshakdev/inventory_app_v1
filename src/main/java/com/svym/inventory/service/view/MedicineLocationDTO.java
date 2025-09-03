package com.svym.inventory.service.view;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class MedicineLocationDTO {
    private String medicineName;
    private Integer numberOfBatches;
    private Long locationId;
    private Long medicineId;
    private Integer totalNumberOfMedicines;
}
