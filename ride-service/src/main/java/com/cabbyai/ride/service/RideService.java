package com.cabbyai.ride.service;

import com.cabbyai.ride.client.DriverClient;
import com.cabbyai.ride.entity.Ride;
import com.cabbyai.ride.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RideService {
    
    @Autowired
    private RideRepository rideRepository;
    
    @Autowired
    private DriverClient driverClient;
    
    public Ride bookRide(Ride ride) {
        // Get available drivers
        List<Map<String, Object>> availableDrivers = driverClient.getAvailableDrivers();
        if (availableDrivers.isEmpty()) {
            throw new RuntimeException("No drivers available");
        }
        
        // Assign first available driver
        Map<String, Object> assignedDriver = availableDrivers.get(0);
        Long driverId = ((Number) assignedDriver.get("driverId")).longValue();
        ride.setDriverId(driverId);
        
        // Calculate estimated fare
        ride.setEstimatedFare(calculateEstimatedFare(ride.getPickupLocation(), ride.getDropoffLocation()));
        
        // Save ride
        Ride savedRide = rideRepository.save(ride);
        
        // Update driver status to BUSY
        try {
            driverClient.updateDriverStatus(driverId, Map.of("status", "BUSY"));
        } catch (Exception e) {
            // Log error but don't fail the ride booking
            System.err.println("Failed to update driver status: " + e.getMessage());
        }
        
        return savedRide;
    }
    
    public Optional<Ride> updateRideStatus(Long rideId, Ride.RideStatus status) {
        Optional<Ride> rideOpt = rideRepository.findById(rideId);
        if (rideOpt.isPresent()) {
            Ride ride = rideOpt.get();
            ride.setStatus(status);
            
            // Set timestamps based on status
            switch (status) {
                case IN_PROGRESS:
                    ride.setStartedAt(LocalDateTime.now());
                    break;
                case COMPLETED:
                    ride.setCompletedAt(LocalDateTime.now());
                    ride.setActualFare(ride.getEstimatedFare()); // In real app, calculate actual fare
                    // Make driver available again
                    try {
                        driverClient.updateDriverStatus(ride.getDriverId(), Map.of("status", "AVAILABLE"));
                    } catch (Exception e) {
                        System.err.println("Failed to update driver status: " + e.getMessage());
                    }
                    break;
                case CANCELLED:
                    // Make driver available again
                    try {
                        driverClient.updateDriverStatus(ride.getDriverId(), Map.of("status", "AVAILABLE"));
                    } catch (Exception e) {
                        System.err.println("Failed to update driver status: " + e.getMessage());
                    }
                    break;
            }
            
            return Optional.of(rideRepository.save(ride));
        }
        return Optional.empty();
    }
    
    public List<Ride> getUserRides(Long userId) {
        return rideRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    public List<Ride> getDriverRides(Long driverId) {
        return rideRepository.findByDriverIdOrderByCreatedAtDesc(driverId);
    }
    
    public Optional<Ride> getRideById(Long rideId) {
        return rideRepository.findById(rideId);
    }
    
    public BigDecimal calculateEstimatedFare(String pickup, String dropoff) {
        // Simplified fare calculation - in real app, would use distance/time
        // Base fare + distance-based calculation
        return new BigDecimal("15.50");
    }
}
