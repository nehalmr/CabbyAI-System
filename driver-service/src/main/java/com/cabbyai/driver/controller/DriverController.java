package com.cabbyai.driver.controller;

import com.cabbyai.driver.entity.Driver;
import com.cabbyai.driver.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/drivers")
@Tag(name = "Driver Management", description = "Driver registration and management")
public class DriverController {
    
    @Autowired
    private DriverService driverService;
    
    @PostMapping("/register")
    @Operation(summary = "Register a new driver")
    public ResponseEntity<?> registerDriver(@RequestBody Driver driver) {
        try {
            Driver registeredDriver = driverService.registerDriver(driver);
            return ResponseEntity.ok(registeredDriver);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/available")
    @Operation(summary = "Get available drivers")
    public ResponseEntity<List<Driver>> getAvailableDrivers() {
        List<Driver> drivers = driverService.getAvailableDrivers();
        return ResponseEntity.ok(drivers);
    }
    
    @PutMapping("/status/{driverId}")
    @Operation(summary = "Update driver status")
    public ResponseEntity<?> updateDriverStatus(@PathVariable Long driverId, @RequestBody Map<String, String> statusData) {
        try {
            Driver.DriverStatus status = Driver.DriverStatus.valueOf(statusData.get("status"));
            Optional<Driver> updatedDriver = driverService.updateDriverStatus(driverId, status);
            if (updatedDriver.isPresent()) {
                return ResponseEntity.ok(updatedDriver.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid status"));
        }
    }
    
    @PutMapping("/location/{driverId}")
    @Operation(summary = "Update driver location")
    public ResponseEntity<?> updateDriverLocation(@PathVariable Long driverId, @RequestBody Map<String, Double> locationData) {
        Double latitude = locationData.get("latitude");
        Double longitude = locationData.get("longitude");
        
        Optional<Driver> updatedDriver = driverService.updateDriverLocation(driverId, latitude, longitude);
        if (updatedDriver.isPresent()) {
            return ResponseEntity.ok(updatedDriver.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{driverId}")
    @Operation(summary = "Get driver by ID")
    public ResponseEntity<?> getDriverById(@PathVariable Long driverId) {
        Optional<Driver> driver = driverService.getDriverById(driverId);
        if (driver.isPresent()) {
            return ResponseEntity.ok(driver.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping
    @Operation(summary = "Get all drivers")
    public ResponseEntity<List<Driver>> getAllDrivers() {
        List<Driver> drivers = driverService.getAllDrivers();
        return ResponseEntity.ok(drivers);
    }
}
