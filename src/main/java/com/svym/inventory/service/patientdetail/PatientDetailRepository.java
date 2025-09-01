package com.svym.inventory.service.patientdetail;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.svym.inventory.service.entity.PatientDetail;

public interface PatientDetailRepository extends JpaRepository<PatientDetail, Long> {

    @Query("SELECT p FROM PatientDetail p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.patientId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.phone) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<PatientDetail> findByNameOrPatientIdOrPhoneContainingIgnoreCase(@Param("searchTerm") String searchTerm);

    boolean existsByPatientId(String patientId);

    boolean existsByPhone(String phone);
}
