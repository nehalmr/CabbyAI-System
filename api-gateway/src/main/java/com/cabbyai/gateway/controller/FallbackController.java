package com.cabbyai.gateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class FallbackController {
    
    private static final Logger logger = LoggerFactory.getLogger(FallbackController.class);
    
    @RequestMapping("/fallback")
    public ResponseEntity<Map<String, Object>> fallback() {
        logger.warn("Fallback endpoint triggered - service unavailable");
        
        Map<String, Object> response = Map.of(
            "error", "Service temporarily unavailable",
            "message", "Please try again later",
            "timestamp", LocalDateTime.now(),
            "status", HttpStatus.SERVICE_UNAVAILABLE.value()
        );
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}
