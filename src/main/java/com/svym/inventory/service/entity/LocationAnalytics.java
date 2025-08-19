package com.svym.inventory.service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "location_analytics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Column(name = "analytics_date", nullable = false)
    private LocalDate analyticsDate;

    @Column(name = "total_purchase_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPurchaseAmount = BigDecimal.ZERO;

    @Column(name = "weekly_total_purchase", nullable = false, precision = 12, scale = 2)
    private BigDecimal weeklyTotalPurchase = BigDecimal.ZERO;

    @Column(name = "month_name", nullable = false, length = 20)
    private String monthName;

    @Column(name = "year_number", nullable = false)
    private Integer yearNumber;

    @Column(name = "week_number", nullable = false)
    private Integer weekNumber;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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
