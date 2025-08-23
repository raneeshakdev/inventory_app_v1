package com.svym.inventory.service.medicinepbatch;

import java.util.List;

import com.svym.inventory.service.dto.LocationMedicineStatusDTO;
import com.svym.inventory.service.dto.MedicinePurchaseBatchDTO;
import com.svym.inventory.service.dto.MedicinePurchaseBatchPartialUpdateDTO;

public interface MedicinePurchaseBatchService {
    MedicinePurchaseBatchDTO create(MedicinePurchaseBatchDTO dto);
    MedicinePurchaseBatchDTO getById(Long id);
    List<MedicinePurchaseBatchDTO> getAll();
    MedicinePurchaseBatchDTO update(Long id, MedicinePurchaseBatchDTO dto);
    MedicinePurchaseBatchDTO partialUpdate(Long id, MedicinePurchaseBatchPartialUpdateDTO dto);
    void delete(Long id);
    void softDelete(Long id);
    List<LocationMedicineStatusDTO> getLocationWiseStatus();
    List<MedicinePurchaseBatchDTO> getAvailableBatchesByLocationAndMedicine(Long locationId, Long medicineId);
    List<MedicinePurchaseBatchDTO> getAvailableBatchesByLocation(Long locationId);
}
