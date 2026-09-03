package com.cabbyai.ride.service;

import com.cabbyai.ride.client.DriverClient;
import com.cabbyai.ride.dto.RideBookingRequest;
import com.cabbyai.ride.entity.Ride;
import com.cabbyai.ride.repository.RideRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RideServiceTest {
    @Mock
    private RideRepository rideRepository;

    @Mock
    private DriverClient driverClient;

    @InjectMocks
    private RideService rideService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void bookingAssignsDriverAndCalculatesFare() {
        RideBookingRequest request = bookingRequest();
        when(driverClient.getAvailableDrivers()).thenReturn(List.of(Map.of("driverId", 7L)));
        when(rideRepository.save(any(Ride.class))).thenAnswer(invocation -> {
            Ride ride = invocation.getArgument(0);
            ride.setRideId(1L);
            return ride;
        });

        Ride result = rideService.bookRide(request);

        assertEquals(7L, result.getDriverId());
        assertEquals(new BigDecimal("15.50"), result.getEstimatedFare());
        verify(driverClient).updateDriverStatus(7L, Map.of("status", "BUSY"));
    }

    @Test
    void bookingWithoutDriversFails() {
        when(driverClient.getAvailableDrivers()).thenReturn(List.of());
        assertThrows(RuntimeException.class, () -> rideService.bookRide(bookingRequest()));
        verifyNoInteractions(rideRepository);
    }

    @Test
    void driverStatusFailureDoesNotCancelBooking() {
        when(driverClient.getAvailableDrivers()).thenReturn(List.of(Map.of("driverId", 7L)));
        when(driverClient.updateDriverStatus(eq(7L), any())).thenThrow(new RuntimeException("driver unavailable"));
        when(rideRepository.save(any(Ride.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertNotNull(rideService.bookRide(bookingRequest()));
        verify(rideRepository).save(any(Ride.class));
    }

    @Test
    void statusTransitionsUpdateTimestampsAndDriver() {
        Ride ride = new Ride();
        ride.setRideId(1L);
        ride.setDriverId(7L);
        ride.setEstimatedFare(new BigDecimal("15.50"));
        when(rideRepository.findById(1L)).thenReturn(Optional.of(ride));
        when(rideRepository.save(ride)).thenReturn(ride);

        rideService.updateRideStatus(1L, Ride.RideStatus.IN_PROGRESS);
        assertNotNull(ride.getStartedAt());
        rideService.updateRideStatus(1L, Ride.RideStatus.COMPLETED);
        assertNotNull(ride.getCompletedAt());
        assertEquals(ride.getEstimatedFare(), ride.getActualFare());
        rideService.updateRideStatus(1L, Ride.RideStatus.CANCELLED);
        verify(driverClient, times(2)).updateDriverStatus(7L, Map.of("status", "AVAILABLE"));
    }

    @Test
    void queryMethodsAndFareCalculationWork() {
        when(rideRepository.findByUserIdOrderByCreatedAtDesc(2L)).thenReturn(List.of());
        when(rideRepository.findByDriverIdOrderByCreatedAtDesc(7L)).thenReturn(List.of());
        Ride ride = new Ride();
        when(rideRepository.findById(1L)).thenReturn(Optional.of(ride));

        assertTrue(rideService.getUserRides(2L).isEmpty());
        assertTrue(rideService.getDriverRides(7L).isEmpty());
        assertSame(ride, rideService.getRideById(1L));
        assertEquals(new BigDecimal("15.50"), rideService.calculateEstimatedFare("A", "B"));
        when(rideRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> rideService.getRideById(2L));
    }

    private RideBookingRequest bookingRequest() {
        RideBookingRequest request = new RideBookingRequest();
        request.setUserId(2L);
        request.setPickupLocation("Downtown");
        request.setDropoffLocation("Airport");
        return request;
    }
}
