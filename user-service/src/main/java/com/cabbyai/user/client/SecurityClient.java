package com.cabbyai.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@FeignClient(name = "security-service", fallback = SecurityClientFallback.class)
public interface SecurityClient {
    
    @PostMapping("/api/auth/generate-token")
    Map<String, Object> generateToken(@RequestBody Map<String, Object> request);
}
