package com.svym.inventory.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing daily cost summary for medicines distributed at specific locations.
 * This entity tracks the daily distribution costs and quantities for medicines.
 * Mapped to the "medicine_daily_cost_summary" table in the database.
 */
@Entity
@Table(name = "medicine_daily_cost_summary",
       indexes = {
           @Index(name = "idx_medicine_daily_cost_summary_dist_date", columnList = "dist_date"),
           @Index(name = "idx_medicine_daily_cost_summary_medicine_location", columnList = "medicine_id, location_id")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDailyCostSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "medicine_id", nullable = false)
    private Long medicineId;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Column(name = "dist_date", nullable = false)
    private LocalDate distDate;

    @Column(name = "number_of_unit", nullable = false)
    private Integer numberOfUnit;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", insertable = false, updatable = false)
    private Medicine medicine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", insertable = false, updatable = false)
    private Location location;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
