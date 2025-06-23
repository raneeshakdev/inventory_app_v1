package com.svym.inventory.service.deliverycentertype;

import java.util.List;

import com.svym.inventory.service.dto.DeliveryCenterTypeDTO;

public interface DeliveryCenterTypeService {
    DeliveryCenterTypeDTO create(DeliveryCenterTypeDTO dto);
    DeliveryCenterTypeDTO getById(Long id);
    List<DeliveryCenterTypeDTO> getAll();
    DeliveryCenterTypeDTO update(Long id, DeliveryCenterTypeDTO dto);
    void delete(Long id);
}
