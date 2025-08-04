package com.svym.inventory.service.purchasetype;

import java.util.List;

import com.svym.inventory.service.dto.PurchaseTypeDTO;

public interface PurchaseTypeService {
    PurchaseTypeDTO create(PurchaseTypeDTO dto);
    PurchaseTypeDTO getById(Long id);
    List<PurchaseTypeDTO> getAll();
    PurchaseTypeDTO update(Long id, PurchaseTypeDTO dto);
    void delete(Long id);
}
