package com.svym.inventory.service.medicinepbatch;

import java.util.List;

import com.svym.inventory.service.dto.MedicinePurchaseBatchDTO;

public interface MedicinePurchaseBatchService {
    MedicinePurchaseBatchDTO create(MedicinePurchaseBatchDTO dto);
    MedicinePurchaseBatchDTO getById(Long id);
    List<MedicinePurchaseBatchDTO> getAll();
    MedicinePurchaseBatchDTO update(Long id, MedicinePurchaseBatchDTO dto);
    void delete(Long id);
}
