package com.svym.inventory.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Represents a type of medicine in the inventory system.
 * This entity contains details about the medicine type such as its name, description,
 * and the timestamp when it was created.
 * This entity is mapped to the "medicine_types" table in the database.
 */
@Entity
@Table(name = "medicine_purchase_batches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicinePurchaseBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @Column(name = "batch_name", nullable = false)
    private String batchName;

    @Column(name = "initial_quantity", nullable = false)
    private Integer initialQuantity;

    @Column(name = "current_quantity", nullable = false)
    private Integer currentQuantity;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_type_id", nullable = false)
    private PurchaseType purchaseType;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.lastUpdatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdatedAt = LocalDateTime.now();
    }
}
