package com.svym.inventory.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "medicine_location_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(MedicineLocationStockId.class)
public class MedicineLocationStock {

    @Id
    @Column(name = "medicine_id")
    private Long medicineId;

    @Id
    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "number_of_batches", nullable = false)
    private Short numberOfBatches = 0;

    @Column(name = "is_out_of_stock", nullable = false)
    private Boolean isOutOfStock = false;

    @Column(name = "has_expired_batches", nullable = false)
    private Boolean hasExpiredBatches = false;

    @Column(name = "total_number_of_medicines", nullable = false)
    private Integer totalNumberOfMedicines = 0;

    @Column(name = "number_of_med_expired", nullable = false)
    private Integer numberOfMedExpired = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", insertable = false, updatable = false)
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", insertable = false, updatable = false)
    private Location location;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @PrePersist
    protected void onCreate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
