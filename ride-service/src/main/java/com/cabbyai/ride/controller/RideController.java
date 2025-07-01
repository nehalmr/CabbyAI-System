package com.cabbyai.ride.controller;

import com.cabbyai.ride.dto.RideBookingRequest;
import com.cabbyai.ride.entity.Ride;
import com.cabbyai.ride.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rides")
@Tag(name = "Ride Management", description = "Ride booking and management")
public class RideController {
    
    private static final Logger logger = LoggerFactory.getLogger(RideController.class);
    
    @Autowired
    private RideService rideService;
    
    @PostMapping("/book")
    @Operation(summary = "Book a new ride")
    public ResponseEntity<Ride> bookRide(@Valid @RequestBody RideBookingRequest request) {
        logger.info("Ride booking request received for user: {}", request.getUserId());
        Ride bookedRide = rideService.bookRide(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookedRide);
    }
    
    @PutMapping("/status/{rideId}")
    @Operation(summary = "Update ride status")
    public ResponseEntity<Ride> updateRideStatus(
            @PathVariable Long rideId, 
            @RequestBody Map<String, String> statusData) {
        logger.info("Status update request for ride: {} to {}", rideId, statusData.get("status"));
        
        Ride.RideStatus status = Ride.RideStatus.valueOf(statusData.get("status"));
        Ride updatedRide = rideService.updateRideStatus(rideId, status);
        return ResponseEntity.ok(updatedRide);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user ride history")
    public ResponseEntity<List<Ride>> getUserRides(@PathVariable Long userId) {
        logger.debug("Ride history request for user: {}", userId);
        List<Ride> rides = rideService.getUserRides(userId);
        return ResponseEntity.ok(rides);
    }
    
    @GetMapping("/driver/{driverId}")
    @Operation(summary = "Get driver ride history")
    public ResponseEntity<List<Ride>> getDriverRides(@PathVariable Long driverId) {
        logger.debug("Ride history request for driver: {}", driverId);
        List<Ride> rides = rideService.getDriverRides(driverId);
        return ResponseEntity.ok(rides);
    }
    
    @GetMapping("/{rideId}")
    @Operation(summary = "Get ride by ID")
    public ResponseEntity<Ride> getRideById(@PathVariable Long rideId) {
        logger.debug("Ride details request for ID: {}", rideId);
        Ride ride = rideService.getRideById(rideId);
        return ResponseEntity.ok(ride);
    }
    
    @PostMapping("/estimate-fare")
    @Operation(summary = "Calculate estimated fare")
    public ResponseEntity<Map<String, BigDecimal>> estimateFare(@RequestBody Map<String, String> locations) {
        String pickup = locations.get("pickupLocation");
        String dropoff = locations.get("dropoffLocation");
        logger.debug("Fare estimation request: {} to {}", pickup, dropoff);
        
        BigDecimal fare = rideService.calculateEstimatedFare(pickup, dropoff);
        return ResponseEntity.ok(Map.of("estimatedFare", fare));
    }
}
