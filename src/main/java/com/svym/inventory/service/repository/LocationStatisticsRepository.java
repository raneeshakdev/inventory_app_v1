package com.svym.inventory.service.repository;

import com.svym.inventory.service.entity.LocationStatistics;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationStatisticsRepository extends CrudRepository<LocationStatistics, Long> {

    Optional<LocationStatistics> findByLocationIdAndIsActiveTrue(Long locationId);

    List<LocationStatistics> findByIsActiveTrueOrderByLocationName();

    @Query("SELECT ls FROM LocationStatistics ls JOIN ls.location l WHERE l.isActive = true AND ls.isActive = true ORDER BY l.name")
    List<LocationStatistics> findAllActiveLocationStatistics();

    @Query("SELECT ls FROM LocationStatistics ls WHERE ls.location.id = :locationId AND ls.isActive = true")
    Optional<LocationStatistics> findActiveByLocationId(@Param("locationId") Long locationId);

    @Query("SELECT ls FROM LocationStatistics ls JOIN ls.location l WHERE l.id IN :locationIds AND l.isActive = true AND ls.isActive = true ORDER BY l.name")
    List<LocationStatistics> findAllActiveLocationStatisticsByLocationIds(@Param("locationIds") List<Long> locationIds);
}
