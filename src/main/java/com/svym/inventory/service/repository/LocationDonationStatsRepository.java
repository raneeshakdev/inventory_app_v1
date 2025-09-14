package com.svym.inventory.service.repository;

import com.svym.inventory.service.entity.LocationDonationStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationDonationStatsRepository extends JpaRepository<LocationDonationStats, Long> {

    @Query("""
        SELECT COALESCE(l.name, CONCAT('Location ', lds.locationId)) as name, SUM(lds.totalDonation) as amount
        FROM LocationDonationStats lds
        LEFT JOIN Location l ON lds.locationId = l.id
        WHERE lds.locationId = :locationId
        AND lds.month = :month
        AND lds.year = :year
        GROUP BY lds.locationId, l.name
        ORDER BY SUM(lds.totalDonation) DESC
        """)
    List<Object[]> findDonationsByLocationAndMonth(@Param("locationId") Long locationId,
                                                  @Param("month") Integer month,
                                                  @Param("year") Integer year);

    @Query("""
        SELECT COALESCE(l.name, CONCAT('Location ', lds.locationId)) as name, SUM(lds.totalDonation) as amount
        FROM LocationDonationStats lds
        LEFT JOIN Location l ON lds.locationId = l.id
        WHERE lds.month = :month
        AND lds.year = :year
        GROUP BY lds.locationId, l.name
        ORDER BY SUM(lds.totalDonation) DESC
        """)
    List<Object[]> findDonationsByMonth(@Param("month") Integer month,
                                       @Param("year") Integer year);

    @Query("""
        SELECT COALESCE(l.name, CONCAT('Location ', lds.locationId)) as name, SUM(lds.totalDonation) as amount
        FROM LocationDonationStats lds
        LEFT JOIN Location l ON lds.locationId = l.id
        WHERE lds.locationId = :locationId
        AND lds.month = :month
        AND lds.year = :year
        GROUP BY lds.locationId, l.name
        ORDER BY SUM(lds.totalDonation) DESC
        """)
    List<Object[]> findCurrentMonthDonationsByLocation(@Param("locationId") Long locationId,
                                                      @Param("month") Integer month,
                                                      @Param("year") Integer year);

    @Query("""
        SELECT COALESCE(l.name, CONCAT('Location ', lds.locationId)) as name, SUM(lds.totalDonation) as amount
        FROM LocationDonationStats lds
        LEFT JOIN Location l ON lds.locationId = l.id
        WHERE lds.month = :month
        AND lds.year = :year
        GROUP BY lds.locationId, l.name
        ORDER BY SUM(lds.totalDonation) DESC
        """)
    List<Object[]> findCurrentMonthDonationsForAllLocations(@Param("month") Integer month,
                                                           @Param("year") Integer year);

    // Add method to get the latest month/year with data
    @Query("""
        SELECT lds.month, lds.year
        FROM LocationDonationStats lds
        ORDER BY lds.year DESC, lds.month DESC
        LIMIT 1
        """)
    List<Object[]> findLatestMonthYear();

    // Add method to get all available months/years
    @Query("""
        SELECT DISTINCT lds.month, lds.year
        FROM LocationDonationStats lds
        ORDER BY lds.year DESC, lds.month DESC
        """)
    List<Object[]> findAllAvailableMonthsYears();

    // Add method to find specific entry by location, year, month, and week
    Optional<LocationDonationStats> findByLocationIdAndYearAndMonthAndWeek(
        Long locationId, Integer year, Integer month, Integer week);
}
