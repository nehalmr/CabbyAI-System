package com.cabbyai.ride.service;

import com.cabbyai.ride.client.DriverClient;
import com.cabbyai.ride.dto.RideBookingRequest;
import com.cabbyai.ride.entity.Ride;
import com.cabbyai.ride.exception.NoDriversAvailableException;
import com.cabbyai.ride.exception.RideNotFoundException;
import com.cabbyai.ride.repository.RideRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RideService {
    
    private static final Logger logger = LoggerFactory.getLogger(RideService.class);
    
    @Autowired
    private RideRepository rideRepository;
    
    @Autowired
    private DriverClient driverClient;
    
    public Ride bookRide(RideBookingRequest request) {
        logger.info("Booking ride for user: {} from {} to {}", 
                   request.getUserId(), request.getPickupLocation(), request.getDropoffLocation());
        
        // Get available drivers
        List<Map<String, Object>> availableDrivers = driverClient.getAvailableDrivers();
        if (availableDrivers.isEmpty()) {
            logger.warn("No drivers available for ride booking");
            throw new NoDriversAvailableException("No drivers are currently available");
        }
        
        // Assign first available driver
        Map<String, Object> assignedDriver = availableDrivers.get(0);
        Long driverId = ((Number) assignedDriver.get("driverId")).longValue();
        
        // Create ride
        Ride ride = new Ride();
        ride.setUserId(request.getUserId());
        ride.setDriverId(driverId);
        ride.setPickupLocation(request.getPickupLocation());
        ride.setDropoffLocation(request.getDropoffLocation());
        ride.setPickupLatitude(request.getPickupLatitude());
        ride.setPickupLongitude(request.getPickupLongitude());
        ride.setDropoffLatitude(request.getDropoffLatitude());
        ride.setDropoffLongitude(request.getDropoffLongitude());
        ride.setEstimatedFare(calculateEstimatedFare(request.getPickupLocation(), request.getDropoffLocation()));
        
        // Save ride
        Ride savedRide = rideRepository.save(ride);
        
        // Update driver status to BUSY
        try {
            driverClient.updateDriverStatus(driverId, Map.of("status", "BUSY"));
            logger.info("Driver {} assigned to ride {}", driverId, savedRide.getRideId());
        } catch (Exception e) {
            logger.error("Failed to update driver status for driver: {}", driverId, e);
            // Don't fail the ride booking, but log the error
        }
        
        logger.info("Ride booked successfully with ID: {}", savedRide.getRideId());
        return savedRide;
    }
    
    public Ride updateRideStatus(Long rideId, Ride.RideStatus status) {
        logger.info("Updating ride {} status to {}", rideId, status);
        
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException("Ride not found with ID: " + rideId));
        
        ride.setStatus(status);
        
        // Set timestamps based on status
        switch (status) {
            case IN_PROGRESS:
                ride.setStartedAt(LocalDateTime.now());
                break;
            case COMPLETED:
                ride.setCompletedAt(LocalDateTime.now());
                ride.setActualFare(ride.getEstimatedFare()); // In real app, calculate actual fare
                makeDriverAvailable(ride.getDriverId());
                break;
            case CANCELLED:
                makeDriverAvailable(ride.getDriverId());
                break;
        }
        
        Ride updatedRide = rideRepository.save(ride);
        logger.info("Ride {} status updated to {}", rideId, status);
        
        return updatedRide;
    }
    
    public List<Ride> getUserRides(Long userId) {
        logger.debug("Fetching rides for user: {}", userId);
        return rideRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    public List<Ride> getDriverRides(Long driverId) {
        logger.debug("Fetching rides for driver: {}", driverId);
        return rideRepository.findByDriverIdOrderByCreatedAtDesc(driverId);
    }
    
    public Ride getRideById(Long rideId) {
        logger.debug("Fetching ride by ID: {}", rideId);
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException("Ride not found with ID: " + rideId));
    }
    
    public BigDecimal calculateEstimatedFare(String pickup, String dropoff) {
        // Simplified fare calculation - in real app, would use distance/time
        // Base fare + distance-based calculation
        logger.debug("Calculating fare for route: {} to {}", pickup, dropoff);
        return new BigDecimal("15.50");
    }
    
    private void makeDriverAvailable(Long driverId) {
        try {
            driverClient.updateDriverStatus(driverId, Map.of("status", "AVAILABLE"));
            logger.info("Driver {} made available", driverId);
        } catch (Exception e) {
            logger.error("Failed to update driver status for driver: {}", driverId, e);
        }
    }
}
