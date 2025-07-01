package com.cabbyai.driver.controller;

import com.cabbyai.driver.dto.DriverRegistrationRequest;
import com.cabbyai.driver.dto.DriverResponse;
import com.cabbyai.driver.dto.LocationUpdateRequest;
import com.cabbyai.driver.entity.Driver;
import com.cabbyai.driver.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/drivers")
@Tag(name = "Driver Management", description = "Driver registration and management")
public class DriverController {
    
    private static final Logger logger = LoggerFactory.getLogger(DriverController.class);
    
    @Autowired
    private DriverService driverService;
    
    @PostMapping("/register")
    @Operation(summary = "Register a new driver")
    public ResponseEntity<DriverResponse> registerDriver(@Valid @RequestBody DriverRegistrationRequest request) {
        logger.info("Driver registration request received for license: {}", request.getLicenseNumber());
        DriverResponse response = driverService.registerDriver(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/available")
    @Operation(summary = "Get available drivers")
    public ResponseEntity<List<DriverResponse>> getAvailableDrivers() {
        logger.debug("Available drivers request received");
        List<DriverResponse> drivers = driverService.getAvailableDrivers();
        return ResponseEntity.ok(drivers);
    }
    
    @PutMapping("/status/{driverId}")
    @Operation(summary = "Update driver status")
    public ResponseEntity<DriverResponse> updateDriverStatus(
            @PathVariable Long driverId, 
            @RequestBody Map<String, String> statusData) {
        logger.info("Status update request for driver: {} to {}", driverId, statusData.get("status"));
        
        Driver.DriverStatus status = Driver.DriverStatus.valueOf(statusData.get("status"));
        DriverResponse response = driverService.updateDriverStatus(driverId, status);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/location/{driverId}")
    @Operation(summary = "Update driver location")
    public ResponseEntity<DriverResponse> updateDriverLocation(
            @PathVariable Long driverId, 
            @Valid @RequestBody LocationUpdateRequest request) {
        logger.debug("Location update request for driver: {}", driverId);
        
        DriverResponse response = driverService.updateDriverLocation(driverId, request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{driverId}")
    @Operation(summary = "Get driver by ID")
    public ResponseEntity<DriverResponse> getDriverById(@PathVariable Long driverId) {
        logger.debug("Driver details request for ID: {}", driverId);
        DriverResponse response = driverService.getDriverById(driverId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all drivers")
    public ResponseEntity<List<DriverResponse>> getAllDrivers() {
        logger.debug("All drivers request received");
        List<DriverResponse> drivers = driverService.getAllDrivers();
        return ResponseEntity.ok(drivers);
    }
    
    @DeleteMapping("/{driverId}")
    @Operation(summary = "Deactivate driver")
    public ResponseEntity<Void> deactivateDriver(@PathVariable Long driverId) {
        logger.info("Deactivation request for driver ID: {}", driverId);
        driverService.deactivateDriver(driverId);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/rating/{driverId}")
    @Operation(summary = "Update driver rating")
    public ResponseEntity<DriverResponse> updateDriverRating(
            @PathVariable Long driverId, 
            @RequestBody Map<String, Double> ratingData) {
        logger.info("Rating update request for driver: {}", driverId);
        
        Double rating = ratingData.get("rating");
        DriverResponse response = driverService.updateDriverRating(driverId, rating);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/ride-count/{driverId}")
    @Operation(summary = "Increment driver ride count")
    public ResponseEntity<DriverResponse> incrementDriverRideCount(@PathVariable Long driverId) {
        logger.debug("Ride count increment request for driver: {}", driverId);
        DriverResponse response = driverService.incrementDriverRideCount(driverId);
        return ResponseEntity.ok(response);
    }
}
