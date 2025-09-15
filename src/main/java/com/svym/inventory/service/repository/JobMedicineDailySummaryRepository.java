package com.svym.inventory.service.repository;

import com.svym.inventory.service.entity.JobMedicineDailySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobMedicineDailySummaryRepository extends JpaRepository<JobMedicineDailySummary, Long> {
    // Custom query methods if needed
}
