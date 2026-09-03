package com.cabbyai.driver.service;

import com.cabbyai.driver.dto.DriverRegistrationRequest;
import com.cabbyai.driver.dto.LocationUpdateRequest;
import com.cabbyai.driver.entity.Driver;
import com.cabbyai.driver.repository.DriverRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DriverServiceTest {
    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverService driverService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerDriverMapsAndSavesRequest() {
        DriverRegistrationRequest request = registrationRequest();
        when(driverRepository.existsByLicenseNumber("LIC-12345")).thenReturn(false);
        when(driverRepository.save(any(Driver.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = driverService.registerDriver(request);

        assertEquals("Alex Driver", response.getName());
        assertEquals("LIC-12345", response.getLicenseNumber());
        verify(driverRepository).save(any(Driver.class));
    }

    @Test
    void registerDriverRejectsDuplicateLicense() {
        when(driverRepository.existsByLicenseNumber("LIC-12345")).thenReturn(true);
        assertThrows(RuntimeException.class, () -> driverService.registerDriver(registrationRequest()));
        verify(driverRepository, never()).save(any());
    }

    @Test
    void availableAndAllDriverQueriesMapResponses() {
        Driver driver = driver();
        when(driverRepository.findAvailableDriversOrderedByRating(Driver.DriverStatus.AVAILABLE)).thenReturn(List.of(driver));
        when(driverRepository.findByActiveTrue()).thenReturn(List.of(driver));

        assertEquals(1, driverService.getAvailableDrivers().size());
        assertEquals(1, driverService.getAllDrivers().size());
    }

    @Test
    void statusLocationRatingAndRideCountUpdateDriver() {
        Driver driver = driver();
        when(driverRepository.findByDriverIdAndActiveTrue(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.save(driver)).thenReturn(driver);

        assertEquals(Driver.DriverStatus.BUSY, driverService.updateDriverStatus(1L, Driver.DriverStatus.BUSY).getStatus());
        var location = new LocationUpdateRequest(12.3, 45.6);
        driverService.updateDriverLocation(1L, location);
        assertEquals(12.3, driver.getCurrentLatitude());
        assertEquals(45.6, driver.getCurrentLongitude());
        assertEquals(4.5, driverService.updateDriverRating(1L, 4.5).getRating());
        assertEquals(1, driverService.incrementDriverRideCount(1L).getTotalRides());
    }

    @Test
    void getAndDeactivateDriverHandleActiveState() {
        Driver driver = driver();
        when(driverRepository.findByDriverIdAndActiveTrue(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.save(driver)).thenReturn(driver);

        assertEquals("Alex Driver", driverService.getDriverById(1L).getName());
        driverService.deactivateDriver(1L);
        assertFalse(driver.isActive());
        assertEquals(Driver.DriverStatus.OFFLINE, driver.getStatus());
    }

    @Test
    void missingDriverIsRejected() {
        when(driverRepository.findByDriverIdAndActiveTrue(1L)).thenReturn(Optional.empty());
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> driverService.getDriverById(1L));
        assertThrows(RuntimeException.class, () -> driverService.updateDriverStatus(1L, Driver.DriverStatus.BUSY));
        assertThrows(RuntimeException.class, () -> driverService.updateDriverLocation(1L, new LocationUpdateRequest(0.0, 0.0)));
        assertThrows(RuntimeException.class, () -> driverService.updateDriverRating(1L, 4.0));
        assertThrows(RuntimeException.class, () -> driverService.incrementDriverRideCount(1L));
        assertThrows(RuntimeException.class, () -> driverService.deactivateDriver(1L));
    }

    private DriverRegistrationRequest registrationRequest() {
        DriverRegistrationRequest request = new DriverRegistrationRequest();
        request.setName("Alex Driver");
        request.setPhone("+14155552671");
        request.setLicenseNumber("LIC-12345");
        request.setVehicleDetails("Toyota sedan, black");
        return request;
    }

    private Driver driver() {
        Driver driver = new Driver("Alex Driver", "+14155552671", "LIC-12345", "Toyota sedan, black");
        driver.setDriverId(1L);
        driver.setTotalRides(0);
        return driver;
    }
}
