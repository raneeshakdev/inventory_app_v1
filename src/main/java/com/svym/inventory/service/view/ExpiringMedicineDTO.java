package com.svym.inventory.service.view;

import java.time.LocalDate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ExpiringMedicineDTO {
    private String medicineName;
    private String batchName;
    private Integer currentQuantity;
    private LocalDate expiryDate;
    private Integer daysToExpiry;
    private String locationName;
}
