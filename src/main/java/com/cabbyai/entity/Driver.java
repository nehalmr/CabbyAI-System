package com.cabbyai.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "drivers")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long driverId;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String phone;
    
    @Column(nullable = false, unique = true)
    private String licenseNumber;
    
    @Column(nullable = false)
    private String vehicleDetails;
    
    @Enumerated(EnumType.STRING)
    private DriverStatus status = DriverStatus.AVAILABLE;
    
    public enum DriverStatus {
        AVAILABLE, BUSY, OFFLINE
    }
    
    // Constructors
    public Driver() {}
    
    public Driver(String name, String phone, String licenseNumber, String vehicleDetails) {
        this.name = name;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
        this.vehicleDetails = vehicleDetails;
    }
    
    // Getters and Setters
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
    
    public DriverStatus getStatus() { return status; }
    public void setStatus(DriverStatus status) { this.status = status; }
}
