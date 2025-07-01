package com.cabbyai.driver.repository;

import com.cabbyai.driver.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    List<Driver> findByStatusAndActiveTrue(Driver.DriverStatus status);
    boolean existsByLicenseNumber(String licenseNumber);
    Optional<Driver> findByDriverIdAndActiveTrue(Long driverId);
    List<Driver> findByActiveTrue();
    
    @Query("SELECT d FROM Driver d WHERE d.status = :status AND d.active = true " +
           "ORDER BY d.rating DESC, d.totalRides DESC")
    List<Driver> findAvailableDriversOrderedByRating(@Param("status") Driver.DriverStatus status);
    
    @Query("SELECT d FROM Driver d WHERE d.active = true AND d.currentLatitude IS NOT NULL " +
           "AND d.currentLongitude IS NOT NULL AND d.status = 'AVAILABLE'")
    List<Driver> findAvailableDriversWithLocation();
}
