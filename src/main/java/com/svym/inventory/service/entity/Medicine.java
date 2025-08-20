package com.svym.inventory.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Represents a medicine in the inventory system.
 * This entity contains details about the medicine such as its name, type,
 * stock information, and timestamps.
 * This entity is mapped to the "medicines" table in the database.
 */
@Entity
@Table(name = "medicines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "medicine_name", nullable = false)
    private String medicineName;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private MedicineType type;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    @Column(name = "current_batches_count", nullable = false)
    private Integer currentBatchesCount = 0;

    @Column(name = "stock_threshold", nullable = false)
    private Integer stockThreshold;

    @Column(name = "out_of_stock", nullable = false)
    private Boolean outOfStock = false;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();
        if (this.currentBatchesCount == null) {
            this.currentBatchesCount = 0;
        }
        if (this.outOfStock == null) {
            this.outOfStock = false;
        }
        if (this.isActive == null) {
            this.isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastModifiedAt = LocalDateTime.now();
    }
}
