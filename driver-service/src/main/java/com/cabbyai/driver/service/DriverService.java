package com.cabbyai.driver.service;

import com.cabbyai.driver.entity.Driver;
import com.cabbyai.driver.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DriverService {
    
    @Autowired
    private DriverRepository driverRepository;
    
    public Driver registerDriver(Driver driver) {
        if (driverRepository.existsByLicenseNumber(driver.getLicenseNumber())) {
            throw new RuntimeException("License number already exists");
        }
        return driverRepository.save(driver);
    }
    
    public List<Driver> getAvailableDrivers() {
        return driverRepository.findByStatusAndActiveTrue(Driver.DriverStatus.AVAILABLE);
    }
    
    public Optional<Driver> updateDriverStatus(Long driverId, Driver.DriverStatus status) {
        Optional<Driver> driverOpt = driverRepository.findByDriverIdAndActiveTrue(driverId);
        if (driverOpt.isPresent()) {
            Driver driver = driverOpt.get();
            driver.setStatus(status);
            return Optional.of(driverRepository.save(driver));
        }
        return Optional.empty();
    }
    
    public Optional<Driver> updateDriverLocation(Long driverId, Double latitude, Double longitude) {
        Optional<Driver> driverOpt = driverRepository.findByDriverIdAndActiveTrue(driverId);
        if (driverOpt.isPresent()) {
            Driver driver = driverOpt.get();
            driver.setCurrentLatitude(latitude);
            driver.setCurrentLongitude(longitude);
            return Optional.of(driverRepository.save(driver));
        }
        return Optional.empty();
    }
    
    public Optional<Driver> getDriverById(Long driverId) {
        return driverRepository.findByDriverIdAndActiveTrue(driverId);
    }
    
    public List<Driver> getAllDrivers() {
        return driverRepository.findByActiveTrue();
    }
}
