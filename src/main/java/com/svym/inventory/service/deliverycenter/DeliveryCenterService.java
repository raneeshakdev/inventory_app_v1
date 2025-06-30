package com.svym.inventory.service.deliverycenter;

import java.util.List;

import com.svym.inventory.service.dto.DeliveryCenterDTO;

public interface DeliveryCenterService {
    DeliveryCenterDTO create(DeliveryCenterDTO dto);
    DeliveryCenterDTO getById(Long id);
    List<DeliveryCenterDTO> getAll();
    DeliveryCenterDTO update(Long id, DeliveryCenterDTO dto);
    void delete(Long id);
}
