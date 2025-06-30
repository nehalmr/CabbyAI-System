package com.cabbyai.driver.repository;

import com.cabbyai.driver.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    List<Driver> findByStatusAndActiveTrue(Driver.DriverStatus status);
    boolean existsByLicenseNumber(String licenseNumber);
    Optional<Driver> findByDriverIdAndActiveTrue(Long driverId);
    List<Driver> findByActiveTrue();
}
