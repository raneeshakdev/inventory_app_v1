package com.svym.inventory.service.repository;

import com.svym.inventory.service.entity.LocationDonationStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationDonationStatsRepository extends JpaRepository<LocationDonationStats, Long> {

    @Query("""
        SELECT l.name as name, SUM(lds.totalDonation) as amount
        FROM LocationDonationStats lds
        JOIN Location l ON lds.locationId = l.id
        WHERE lds.locationId = :locationId
        AND lds.month = :month
        AND lds.year = :year
        GROUP BY l.id, l.name
        ORDER BY SUM(lds.totalDonation) DESC
        """)
    List<Object[]> findDonationsByLocationAndMonth(@Param("locationId") Long locationId,
                                                  @Param("month") Integer month,
                                                  @Param("year") Integer year);

    @Query("""
        SELECT l.name as name, SUM(lds.totalDonation) as amount
        FROM LocationDonationStats lds
        JOIN Location l ON lds.locationId = l.id
        WHERE lds.month = :month
        AND lds.year = :year
        GROUP BY l.id, l.name
        ORDER BY SUM(lds.totalDonation) DESC
        """)
    List<Object[]> findDonationsByMonth(@Param("month") Integer month,
                                       @Param("year") Integer year);

    @Query("""
        SELECT l.name as name, SUM(lds.totalDonation) as amount
        FROM LocationDonationStats lds
        JOIN Location l ON lds.locationId = l.id
        WHERE lds.locationId = :locationId
        AND lds.month = EXTRACT(MONTH FROM CURRENT_DATE)
        AND lds.year = EXTRACT(YEAR FROM CURRENT_DATE)
        GROUP BY l.id, l.name
        ORDER BY SUM(lds.totalDonation) DESC
        """)
    List<Object[]> findCurrentMonthDonationsByLocation(@Param("locationId") Long locationId);

    @Query("""
        SELECT l.name as name, SUM(lds.totalDonation) as amount
        FROM LocationDonationStats lds
        JOIN Location l ON lds.locationId = l.id
        WHERE lds.month = EXTRACT(MONTH FROM CURRENT_DATE)
        AND lds.year = EXTRACT(YEAR FROM CURRENT_DATE)
        GROUP BY l.id, l.name
        ORDER BY SUM(lds.totalDonation) DESC
        """)
    List<Object[]> findCurrentMonthDonationsForAllLocations();
}
