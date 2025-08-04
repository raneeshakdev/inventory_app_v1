package com.svym.inventory.service.common;

import java.util.Optional;
import java.util.function.Supplier;

public class CommonUtil {
	/**
	 * Populates the sortBy field of the CommonCriteriaVo with the provided module name.
	 * 
	 * @param criteriaVo The CommonCriteriaVo object to be populated.
	 * @param module     The module name to set in the sortBy field.
	 */
	private CommonUtil() {
		// Private constructor to prevent instantiation
	}
	
	public static void criteriaVoPopulator(CommonCriteriaVo criteriaVo, String module) {
		if (null != criteriaVo && !module.isBlank() && !criteriaVo.getSortBy().isBlank()) {
			criteriaVo.setSortBy(module);
		}
	}
	
	public static <T> Optional<T> safeGet(Supplier<T> supplier) {
		try {
			T result = supplier.get();
			return Optional.ofNullable(result);
		} catch (Exception e) {
			// Log the exception if needed
			return Optional.empty();
		}
	}
}
