package com.cabbyai.driver.service;

import com.cabbyai.driver.dto.DriverRegistrationRequest;
import com.cabbyai.driver.dto.DriverResponse;
import com.cabbyai.driver.dto.LocationUpdateRequest;
import com.cabbyai.driver.entity.Driver;
import com.cabbyai.driver.exception.DriverNotFoundException;
import com.cabbyai.driver.exception.LicenseAlreadyExistsException;
import com.cabbyai.driver.repository.DriverRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DriverService {
    
    private static final Logger logger = LoggerFactory.getLogger(DriverService.class);
    
    @Autowired
    private DriverRepository driverRepository;
    
    public DriverResponse registerDriver(DriverRegistrationRequest request) {
        logger.info("Attempting to register driver with license: {}", request.getLicenseNumber());
        
        if (driverRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            logger.warn("Registration failed - license number already exists: {}", request.getLicenseNumber());
            throw new LicenseAlreadyExistsException("License number already exists: " + request.getLicenseNumber());
        }
        
        Driver driver = new Driver();
        driver.setName(request.getName());
        driver.setPhone(request.getPhone());
        driver.setLicenseNumber(request.getLicenseNumber());
        driver.setVehicleDetails(request.getVehicleDetails());
        
        Driver savedDriver = driverRepository.save(driver);
        logger.info("Driver registered successfully with ID: {}", savedDriver.getDriverId());
        
        return mapToDriverResponse(savedDriver);
    }
    
    @Transactional(readOnly = true)
    public List<DriverResponse> getAvailableDrivers() {
        logger.debug("Fetching available drivers");
        
        List<Driver> drivers = driverRepository.findAvailableDriversOrderedByRating(Driver.DriverStatus.AVAILABLE);
        
        logger.debug("Found {} available drivers", drivers.size());
        return drivers.stream()
                .map(this::mapToDriverResponse)
                .collect(Collectors.toList());
    }
    
    public DriverResponse updateDriverStatus(Long driverId, Driver.DriverStatus status) {
        logger.info("Updating driver {} status to {}", driverId, status);
        
        Driver driver = driverRepository.findByDriverIdAndActiveTrue(driverId)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found with ID: " + driverId));
        
        driver.setStatus(status);
        Driver updatedDriver = driverRepository.save(driver);
        
        logger.info("Driver {} status updated to {}", driverId, status);
        return mapToDriverResponse(updatedDriver);
    }
    
    public DriverResponse updateDriverLocation(Long driverId, LocationUpdateRequest request) {
        logger.debug("Updating driver {} location to lat: {}, lng: {}", 
                    driverId, request.getLatitude(), request.getLongitude());
        
        Driver driver = driverRepository.findByDriverIdAndActiveTrue(driverId)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found with ID: " + driverId));
        
        driver.setCurrentLatitude(request.getLatitude());
        driver.setCurrentLongitude(request.getLongitude());
        Driver updatedDriver = driverRepository.save(driver);
        
        logger.debug("Driver {} location updated successfully", driverId);
        return mapToDriverResponse(updatedDriver);
    }
    
    @Transactional(readOnly = true)
    public DriverResponse getDriverById(Long driverId) {
        logger.debug("Fetching driver by ID: {}", driverId);
        
        Driver driver = driverRepository.findByDriverIdAndActiveTrue(driverId)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found with ID: " + driverId));
        
        return mapToDriverResponse(driver);
    }
    
    @Transactional(readOnly = true)
    public List<DriverResponse> getAllDrivers() {
        logger.debug("Fetching all active drivers");
        
        List<Driver> drivers = driverRepository.findByActiveTrue();
        return drivers.stream()
                .map(this::mapToDriverResponse)
                .collect(Collectors.toList());
    }
    
    public void deactivateDriver(Long driverId) {
        logger.info("Deactivating driver with ID: {}", driverId);
        
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found with ID: " + driverId));
        
        driver.setActive(false);
        driver.setStatus(Driver.DriverStatus.OFFLINE);
        driverRepository.save(driver);
        
        logger.info("Driver deactivated successfully: {}", driverId);
    }
    
    public DriverResponse updateDriverRating(Long driverId, Double newRating) {
        logger.info("Updating driver {} rating to {}", driverId, newRating);
        
        Driver driver = driverRepository.findByDriverIdAndActiveTrue(driverId)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found with ID: " + driverId));
        
        driver.setRating(newRating);
        Driver updatedDriver = driverRepository.save(driver);
        
        logger.info("Driver {} rating updated to {}", driverId, newRating);
        return mapToDriverResponse(updatedDriver);
    }
    
    public DriverResponse incrementDriverRideCount(Long driverId) {
        logger.debug("Incrementing ride count for driver: {}", driverId);
        
        Driver driver = driverRepository.findByDriverIdAndActiveTrue(driverId)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found with ID: " + driverId));
        
        driver.setTotalRides(driver.getTotalRides() + 1);
        Driver updatedDriver = driverRepository.save(driver);
        
        logger.debug("Driver {} ride count incremented to {}", driverId, updatedDriver.getTotalRides());
        return mapToDriverResponse(updatedDriver);
    }
    
    private DriverResponse mapToDriverResponse(Driver driver) {
        return new DriverResponse(
            driver.getDriverId(),
            driver.getName(),
            driver.getPhone(),
            driver.getLicenseNumber(),
            driver.getVehicleDetails(),
            driver.getStatus(),
            driver.getCreatedAt(),
            driver.getUpdatedAt(),
            driver.getCurrentLatitude(),
            driver.getCurrentLongitude(),
            driver.getRating(),
            driver.getTotalRides()
        );
    }
}
