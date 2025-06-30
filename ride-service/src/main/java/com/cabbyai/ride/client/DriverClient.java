package com.cabbyai.ride.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.Map;

@FeignClient(name = "driver-service")
public interface DriverClient {
    
    @GetMapping("/api/drivers/available")
    List<Map<String, Object>> getAvailableDrivers();
    
    @PutMapping("/api/drivers/status/{driverId}")
    Map<String, Object> updateDriverStatus(@PathVariable Long driverId, @RequestBody Map<String, String> statusData);
    
    @GetMapping("/api/drivers/{driverId}")
    Map<String, Object> getDriverById(@PathVariable Long driverId);
}
