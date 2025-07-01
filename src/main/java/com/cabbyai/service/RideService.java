package com.cabbyai.service;

import com.cabbyai.entity.Driver;
import com.cabbyai.entity.Ride;
import com.cabbyai.repository.DriverRepository;
import com.cabbyai.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class RideService {
    
    @Autowired
    private RideRepository rideRepository;
    
    @Autowired
    private DriverRepository driverRepository;
    
    public Ride bookRide(Ride ride) {
        // Find available driver
        List<Driver> availableDrivers = driverRepository.findByStatus(Driver.DriverStatus.AVAILABLE);
        if (availableDrivers.isEmpty()) {
            throw new RuntimeException("No drivers available");
        }
        
        Driver assignedDriver = availableDrivers.get(0);
        ride.setDriverId(assignedDriver.getDriverId());
        
        // Calculate fare (simplified calculation)
        ride.setFare(calculateFare(ride.getPickupLocation(), ride.getDropoffLocation()));
        
        // Update driver status
        assignedDriver.setStatus(Driver.DriverStatus.BUSY);
        driverRepository.save(assignedDriver);
        
        return rideRepository.save(ride);
    }
    
    public Optional<Ride> updateRideStatus(Long rideId, Ride.RideStatus status) {
        Optional<Ride> rideOpt = rideRepository.findById(rideId);
        if (rideOpt.isPresent()) {
            Ride ride = rideOpt.get();
            ride.setStatus(status);
            
            // If ride is completed, make driver available again
            if (status == Ride.RideStatus.COMPLETED) {
                Optional<Driver> driverOpt = driverRepository.findById(ride.getDriverId());
                if (driverOpt.isPresent()) {
                    Driver driver = driverOpt.get();
                    driver.setStatus(Driver.DriverStatus.AVAILABLE);
                    driverRepository.save(driver);
                }
            }
            
            return Optional.of(rideRepository.save(ride));
        }
        return Optional.empty();
    }
    
    public List<Ride> getUserRides(Long userId) {
        return rideRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    private BigDecimal calculateFare(String pickup, String dropoff) {
        // Simplified fare calculation - in real app, would use distance/time
        return new BigDecimal("15.50");
    }
}
