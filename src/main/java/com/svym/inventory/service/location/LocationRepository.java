package com.svym.inventory.service.location;

import com.svym.inventory.service.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
