package com.svym.inventory.service.distributiontype;

import java.util.List;

import com.svym.inventory.service.dto.DistributionTypeDTO;

public interface DistributionTypeService {
    DistributionTypeDTO create(DistributionTypeDTO dto);
    DistributionTypeDTO update(Long id, DistributionTypeDTO dto);
    DistributionTypeDTO getById(Long id);
    List<DistributionTypeDTO> getAll();
    void delete(Long id);
}
