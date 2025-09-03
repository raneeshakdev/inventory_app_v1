package com.svym.inventory.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDistributionViewDTO {
    private Long patientId;
    private String patientExternalId;
    private String patientName;
    private Long deliveryCenterId;
    private LocalDate distributionDate;
    private List<MedicineDistributionItemViewDTO> medicines;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MedicineDistributionItemViewDTO {
        private Long distributionId;
        private Long medicineId;
        private String medicineName;
        private Long batchId;
        private String batchName;
        private Long locationId;
        private Integer quantity;
        private Double unitPrice;
        private Double totalPrice;
    }
}
