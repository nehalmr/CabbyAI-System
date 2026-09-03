package com.cabbyai.security.service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", "mySecretKey1234567890123456789012345678901234567890");
        ReflectionTestUtils.setField(jwtService, "expiration", 86_400_000L);
    }

    @Test
    void generatedTokenContainsExpectedClaims() {
        String token = jwtService.generateToken(42L, "user@example.com", "USER");

        assertTrue(jwtService.validateToken(token));
        assertEquals("user@example.com", jwtService.extractEmail(token));
        assertEquals(42L, jwtService.extractUserId(token));
        assertEquals("USER", jwtService.extractRole(token));
        assertFalse(jwtService.isTokenExpired(token));
        assertEquals(86_400_000L, jwtService.getExpirationTime());
    }

    @Test
    void malformedAndExpiredTokensAreRejected() {
        assertFalse(jwtService.validateToken("not-a-jwt"));
        ReflectionTestUtils.setField(jwtService, "expiration", -1L);
        String expiredToken = jwtService.generateToken(42L, "user@example.com", "USER");
        assertFalse(jwtService.validateToken(expiredToken));
    }

    @Test
    void claimsCanBeExtractedFromGeneratedToken() {
        String token = jwtService.generateToken(7L, "driver@example.com", "DRIVER");
        Claims claims = jwtService.extractAllClaims(token);

        assertEquals("driver@example.com", claims.getSubject());
        assertEquals(7, claims.get("userId", Integer.class));
        assertEquals("DRIVER", claims.get("role", String.class));
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }
}
