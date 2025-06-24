package com.svym.inventory.service.location;

import com.svym.inventory.service.dto.LocationDTO;
import java.util.List;

public interface LocationService {
    LocationDTO create(LocationDTO dto);
    LocationDTO update(Long id, LocationDTO dto);
    LocationDTO getById(Long id);
    List<LocationDTO> getAll();
    void delete(Long id);
}
