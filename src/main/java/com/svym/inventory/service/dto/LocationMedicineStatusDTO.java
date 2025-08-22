package com.svym.inventory.service.dto;

import com.svym.inventory.service.entity.Location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationMedicineStatusDTO {
	
	private Location location;
	private Long stockStatus;
	private Long expired;
	private Long expiringSoon;
}
