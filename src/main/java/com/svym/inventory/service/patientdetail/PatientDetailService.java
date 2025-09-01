package com.svym.inventory.service.patientdetail;

import java.util.List;

import com.svym.inventory.service.dto.PatientDetailDTO;

public interface PatientDetailService {
    PatientDetailDTO create(PatientDetailDTO dto);
    List<PatientDetailDTO> getAll();
    PatientDetailDTO getById(Long id);
    PatientDetailDTO update(Long id, PatientDetailDTO dto);
    void delete(Long id);
    List<PatientDetailDTO> searchPatients(String searchTerm);
}
