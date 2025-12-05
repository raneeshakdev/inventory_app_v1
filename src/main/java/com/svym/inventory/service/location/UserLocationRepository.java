package com.svym.inventory.service.location;

import com.svym.inventory.service.entity.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {

    @Query("SELECT ul FROM UserLocation ul WHERE ul.userId = :userId AND ul.isActive = true")
    List<UserLocation> findActiveLocationsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(ul) FROM UserLocation ul WHERE ul.userId = :userId AND ul.isActive = true")
    long countActiveLocationsByUserId(@Param("userId") Long userId);
}

