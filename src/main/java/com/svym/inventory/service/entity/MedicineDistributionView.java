package com.svym.inventory.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "medicine_distribution_view")
@IdClass(MedicineDistributionView.MedicineDistributionViewId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MedicineDistributionView {

    @Id
    @Column(name = "distribution_id")
    private Long distributionId;

    @Id
    @Column(name = "medicine_id")
    private Long medicineId;

    @Id
    @Column(name = "batch_id")
    private Long batchId;

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

    @Column(name = "batch_name")
    private String batchName;

    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "medicine_name")
    private String medicineName;

    @Column(name = "patient_external_id")
    private String patientExternalId;

    @Column(name = "patient_name")
    private String patientName;

    // Composite key class
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MedicineDistributionViewId implements Serializable {
        private Long distributionId;
        private Long medicineId;
        private Long batchId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MedicineDistributionViewId that = (MedicineDistributionViewId) o;
            return Objects.equals(distributionId, that.distributionId) &&
                   Objects.equals(medicineId, that.medicineId) &&
                   Objects.equals(batchId, that.batchId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(distributionId, medicineId, batchId);
        }
    }
}
