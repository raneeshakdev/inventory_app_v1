package com.svym.inventory.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Represents a type of delivery center in the inventory system.
 * This entity contains details about the delivery center type such as its name, description,
 * and the timestamp when it was created.
 * This entity is mapped to the "delivery_center_types" table in the database.
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

    @Column(name = "name", nullable = false)
    private String name;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private MedicineType type;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false)
    private String createdBy; // This might reference a user ID, but diagram shows 'created_by' as a string. Assuming it's a name or identifier.

    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "current_batches_count", nullable = false)
    private Integer currentBatchesCount;

    @Column(name = "stock_threshold", nullable = false)
    private Integer stockThreshold;

    @Column(name = "out_of_stock", nullable = false)
    private Boolean outOfStock;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;



    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastModifiedAt = LocalDateTime.now();
    }
}
