package com.cabbyai.driver.dto;

import com.cabbyai.driver.entity.Driver;
import java.time.LocalDateTime;

public class DriverResponse {
    private Long driverId;
    private String name;
    private String phone;
    private String licenseNumber;
    private String vehicleDetails;
    private Driver.DriverStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double currentLatitude;
    private Double currentLongitude;
    private Double rating;
    private Integer totalRides;
    
    public DriverResponse() {}
    
    public DriverResponse(Long driverId, String name, String phone, String licenseNumber,
                         String vehicleDetails, Driver.DriverStatus status, LocalDateTime createdAt,
                         LocalDateTime updatedAt, Double currentLatitude, Double currentLongitude,
                         Double rating, Integer totalRides) {
        this.driverId = driverId;
        this.name = name;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
        this.vehicleDetails = vehicleDetails;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.rating = rating;
        this.totalRides = totalRides;
    }
    
    // Getters and setters
    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    
    public String getVehicleDetails() { return vehicleDetails; }
    public void setVehicleDetails(String vehicleDetails) { this.vehicleDetails = vehicleDetails; }
    
    public Driver.DriverStatus getStatus() { return status; }
    public void setStatus(Driver.DriverStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Double getCurrentLatitude() { return currentLatitude; }
    public void setCurrentLatitude(Double currentLatitude) { this.currentLatitude = currentLatitude; }
    
    public Double getCurrentLongitude() { return currentLongitude; }
    public void setCurrentLongitude(Double currentLongitude) { this.currentLongitude = currentLongitude; }
    
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    
    public Integer getTotalRides() { return totalRides; }
    public void setTotalRides(Integer totalRides) { this.totalRides = totalRides; }
}
