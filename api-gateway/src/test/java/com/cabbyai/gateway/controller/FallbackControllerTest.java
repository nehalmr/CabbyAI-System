package com.cabbyai.gateway.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FallbackControllerTest {
    private final FallbackController controller = new FallbackController();

    @Test
    void fallbackReturnsServiceUnavailablePayload() {
        ResponseEntity<Map<String, Object>> response = controller.fallback();

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Service temporarily unavailable", response.getBody().get("error"));
        assertEquals(503, response.getBody().get("status"));
        assertNotNull(response.getBody().get("timestamp"));
    }
}
