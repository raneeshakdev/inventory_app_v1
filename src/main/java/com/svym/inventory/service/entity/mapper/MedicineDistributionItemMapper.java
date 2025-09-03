package com.svym.inventory.service.entity.mapper;

import org.springframework.stereotype.Component;

import com.svym.inventory.service.dto.MedicineDistributionItemDTO;
import com.svym.inventory.service.entity.MedicineDistributionItem;

@Component
public class MedicineDistributionItemMapper {

	public MedicineDistributionItemDTO toDTO(MedicineDistributionItem entity) {
		MedicineDistributionItemDTO dto = new MedicineDistributionItemDTO();
		dto.setId(entity.getId());
		dto.setQuantity(entity.getQuantity());
		dto.setUnitPrice(entity.getUnitPrice());
		dto.setTotalPrice(entity.getTotalPrice());

		// Set medicine info
		if (entity.getMedicine() != null) {
			MedicineDistributionItemDTO.MedicineDTO medicineDTO = new MedicineDistributionItemDTO.MedicineDTO();
			medicineDTO.setId(entity.getMedicine().getId());
			dto.setMedicine(medicineDTO);
		}

		// Set batch info
		if (entity.getBatch() != null) {
			MedicineDistributionItemDTO.BatchDTO batchDTO = new MedicineDistributionItemDTO.BatchDTO();
			batchDTO.setId(entity.getBatch().getId());
			dto.setBatch(batchDTO);
		}

		return dto;
	}
}
