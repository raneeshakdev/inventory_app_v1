package com.svym.inventory.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "medicine_distribution_view")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDistributionView {

    @Id
    @Column(name = "distribution_id")
    private Long distributionId;

    @Column(name = "delivery_center_id")
    private Long deliveryCenterId;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "distribution_date")
    private LocalDate distributionDate;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "batch_id")
    private Long batchId;

    @Column(name = "batch_name")
    private String batchName;

    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "medicine_id")
    private Long medicineId;

    @Column(name = "medicine_name")
    private String medicineName;

    @Column(name = "patient_external_id")
    private String patientExternalId;

    @Column(name = "patient_name")
    private String patientName;
}
