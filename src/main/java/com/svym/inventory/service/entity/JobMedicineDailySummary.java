package com.svym.inventory.service.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_medicine_daily_summary", schema = "public",
    uniqueConstraints = @UniqueConstraint(name = "uq_medicine_type_summary_date", columnNames = {"medicine_type_id", "summary_date", "job_run_date"}))
public class JobMedicineDailySummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "summary_date", nullable = false)
    private LocalDate summaryDate;

    @Column(name = "job_run_date", nullable = false)
    private LocalDate jobRunDate;

    @Column(name = "medicine_type_id", nullable = false)
    private Long medicineTypeId;

    @Column(name = "purchase_count", nullable = false)
    private Integer purchaseCount = 0;

    @Column(name = "donation_count", nullable = false)
    private Integer donationCount = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getSummaryDate() { return summaryDate; }
    public void setSummaryDate(LocalDate summaryDate) { this.summaryDate = summaryDate; }
    public LocalDate getJobRunDate() { return jobRunDate; }
    public void setJobRunDate(LocalDate jobRunDate) { this.jobRunDate = jobRunDate; }
    public Long getMedicineTypeId() { return medicineTypeId; }
    public void setMedicineTypeId(Long medicineTypeId) { this.medicineTypeId = medicineTypeId; }
    public Integer getPurchaseCount() { return purchaseCount; }
    public void setPurchaseCount(Integer purchaseCount) { this.purchaseCount = purchaseCount; }
    public Integer getDonationCount() { return donationCount; }
    public void setDonationCount(Integer donationCount) { this.donationCount = donationCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

