package com.svym.inventory.service.location;

import com.svym.inventory.service.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("SELECT l FROM Location l WHERE l.isDelete = false OR l.isDelete IS NULL")
    List<Location> findAllActiveLocations();
}
