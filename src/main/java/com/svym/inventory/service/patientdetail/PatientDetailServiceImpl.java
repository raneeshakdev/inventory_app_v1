package com.svym.inventory.service.patientdetail;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.svym.inventory.service.dto.PatientDetailDTO;
import com.svym.inventory.service.entity.PatientDetail;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientDetailServiceImpl implements PatientDetailService {

    private final PatientDetailRepository repository;

    private final ModelMapper modelMapper;

    @Override
    public PatientDetailDTO create(PatientDetailDTO dto) {
        PatientDetail entity = modelMapper.map(dto, PatientDetail.class);
        return modelMapper.map(repository.save(entity), PatientDetailDTO.class);
    }

    @Override
    public List<PatientDetailDTO> getAll() {
        return repository.findAll().stream()
                .map(entity -> modelMapper.map(entity, PatientDetailDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PatientDetailDTO getById(Long id) {
        PatientDetail entity = repository.findById(id).orElseThrow();
        return modelMapper.map(entity, PatientDetailDTO.class);
    }

    @Override
    public PatientDetailDTO update(Long id, PatientDetailDTO dto) {
        PatientDetail entity = repository.findById(id).orElseThrow();
        modelMapper.map(dto, entity);
        entity.setId(id);
        return modelMapper.map(repository.save(entity), PatientDetailDTO.class);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
