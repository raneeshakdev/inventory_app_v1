package com.svym.inventory.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "medicina_distribution")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDistribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false) // Assuming patient_id refers to patient_details
    private PatientDetail patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "distribution_type_id", nullable = false)
    private DistributionType distributionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_center_id", nullable = false)
    private DeliveryCenter deliveryCenter;

    @Column(name = "total_items", nullable = false)
    private Integer totalItems;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "distribution", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<MedicineDistributionItem> distributionItems = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}