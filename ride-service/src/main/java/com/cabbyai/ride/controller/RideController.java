package com.cabbyai.ride.controller;

import com.cabbyai.ride.entity.Ride;
import com.cabbyai.ride.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/rides")
@Tag(name = "Ride Management", description = "Ride booking and management")
public class RideController {
    
    @Autowired
    private RideService rideService;
    
    @PostMapping("/book")
    @Operation(summary = "Book a new ride")
    public ResponseEntity<?> bookRide(@RequestBody Ride ride) {
        try {
            Ride bookedRide = rideService.bookRide(ride);
            return ResponseEntity.ok(bookedRide);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/status/{rideId}")
    @Operation(summary = "Update ride status")
    public ResponseEntity<?> updateRideStatus(@PathVariable Long rideId, @RequestBody Map<String, String> statusData) {
        try {
            Ride.RideStatus status = Ride.RideStatus.valueOf(statusData.get("status"));
            Optional<Ride> updatedRide = rideService.updateRideStatus(rideId, status);
            if (updatedRide.isPresent()) {
                return ResponseEntity.ok(updatedRide.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid status"));
        }
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user ride history")
    public ResponseEntity<List<Ride>> getUserRides(@PathVariable Long userId) {
        List<Ride> rides = rideService.getUserRides(userId);
        return ResponseEntity.ok(rides);
    }
    
    @GetMapping("/driver/{driverId}")
    @Operation(summary = "Get driver ride history")
    public ResponseEntity<List<Ride>> getDriverRides(@PathVariable Long driverId) {
        List<Ride> rides = rideService.getDriverRides(driverId);
        return ResponseEntity.ok(rides);
    }
    
    @GetMapping("/{rideId}")
    @Operation(summary = "Get ride by ID")
    public ResponseEntity<?> getRideById(@PathVariable Long rideId) {
        Optional<Ride> ride = rideService.getRideById(rideId);
        if (ride.isPresent()) {
            return ResponseEntity.ok(ride.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/estimate-fare")
    @Operation(summary = "Calculate estimated fare")
    public ResponseEntity<Map<String, BigDecimal>> estimateFare(@RequestBody Map<String, String> locations) {
        String pickup = locations.get("pickupLocation");
        String dropoff = locations.get("dropoffLocation");
        BigDecimal fare = rideService.calculateEstimatedFare(pickup, dropoff);
        return ResponseEntity.ok(Map.of("estimatedFare", fare));
    }
}
