package com.svym.inventory.service.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonCriteriaVo {
	private Integer pageNumber = 0;
	private Integer pageSize = 1;
	private String sortBy;
	private String search = "";
}
