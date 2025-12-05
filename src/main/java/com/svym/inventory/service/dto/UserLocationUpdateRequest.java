package com.svym.inventory.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLocationUpdateRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Location IDs are required")
    private List<Long> locationIds;
}

