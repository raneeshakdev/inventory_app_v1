package com.svym.inventory.service.entity.mapper;

import com.svym.inventory.service.dto.MedicineDailyCostSummaryDTO;
import com.svym.inventory.service.entity.MedicineDailyCostSummary;
import com.svym.inventory.service.repository.MedicineRepository;
import com.svym.inventory.service.location.LocationRepository;
import com.svym.inventory.service.deliverycenter.DeliveryCenterRepository;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MedicineDailyCostSummaryMapper {

    private final MedicineRepository medicineRepository;
    private final LocationRepository locationRepository;
    private final DeliveryCenterRepository deliveryCenterRepository;

    public MedicineDailyCostSummaryDTO toDto(MedicineDailyCostSummary entity) {
        if (entity == null) return null;

        MedicineDailyCostSummaryDTO dto = new MedicineDailyCostSummaryDTO();
        dto.setId(entity.getId());
        dto.setMedicineId(entity.getMedicineId());
        dto.setLocationId(entity.getLocationId());
        dto.setDeliveryCenterId(entity.getDeliveryCenterId());
        dto.setDistDate(entity.getDistDate());
        dto.setNumberOfUnit(entity.getNumberOfUnit());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        // Set medicine name if medicine relationship is loaded
        if (entity.getMedicine() != null) {
            dto.setMedicineName(entity.getMedicine().getMedicineName());
        }

        // Set location name if location relationship is loaded
        if (entity.getLocation() != null) {
            dto.setLocationName(entity.getLocation().getName());
        }

        // Set delivery center name if delivery center relationship is loaded
        if (entity.getDeliveryCenter() != null) {
            dto.setDeliveryCenterName(entity.getDeliveryCenter().getName());
        }

        return dto;
    }

    public MedicineDailyCostSummary toEntity(MedicineDailyCostSummaryDTO dto) {
        if (dto == null) return null;

        MedicineDailyCostSummary entity = new MedicineDailyCostSummary();
        entity.setId(dto.getId());
        entity.setMedicineId(dto.getMedicineId());
        entity.setLocationId(dto.getLocationId());
        entity.setDeliveryCenterId(dto.getDeliveryCenterId());
        entity.setDistDate(dto.getDistDate());
        entity.setNumberOfUnit(dto.getNumberOfUnit());
        entity.setTotalPrice(dto.getTotalPrice());

        return entity;
    }

    public void updateEntityFromDto(MedicineDailyCostSummaryDTO dto, MedicineDailyCostSummary entity) {
        if (dto == null || entity == null) return;

        entity.setMedicineId(dto.getMedicineId());
        entity.setLocationId(dto.getLocationId());
        entity.setDeliveryCenterId(dto.getDeliveryCenterId());
        entity.setDistDate(dto.getDistDate());
        entity.setNumberOfUnit(dto.getNumberOfUnit());
        entity.setTotalPrice(dto.getTotalPrice());
    }
}
