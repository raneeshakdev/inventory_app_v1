package com.svym.inventory.service.userlocation;

import com.svym.inventory.service.dto.UserLocationDTO;
import com.svym.inventory.service.dto.UserLocationUpdateRequest;
import java.util.List;

public interface UserLocationService {
    List<UserLocationDTO> getUserLocationMappings(Long userId);
    List<UserLocationDTO> updateUserLocationMappings(UserLocationUpdateRequest request);
}

