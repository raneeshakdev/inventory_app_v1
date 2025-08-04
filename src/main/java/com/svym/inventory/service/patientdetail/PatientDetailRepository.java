package com.svym.inventory.service.patientdetail;

import org.springframework.data.jpa.repository.JpaRepository;

import com.svym.inventory.service.entity.PatientDetail;

public interface PatientDetailRepository extends JpaRepository<PatientDetail, Long> {
}
