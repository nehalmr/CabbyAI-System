package com.cabbyai.security.controller;

import com.cabbyai.security.dto.TokenRequest;
import com.cabbyai.security.dto.TokenResponse;
import com.cabbyai.security.dto.ValidationRequest;
import com.cabbyai.security.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "JWT token management")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private JwtService jwtService;
    
    @PostMapping("/generate-token")
    @Operation(summary = "Generate JWT token")
    public ResponseEntity<TokenResponse> generateToken(@Valid @RequestBody TokenRequest request) {
        logger.info("Token generation request for user: {}", request.getUserId());
        
        String token = jwtService.generateToken(request.getUserId(), request.getEmail(), request.getRole());
        TokenResponse response = new TokenResponse(token, "Bearer", jwtService.getExpirationTime());
        
        logger.info("Token generated successfully for user: {}", request.getUserId());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/validate-token")
    @Operation(summary = "Validate JWT token")
    public ResponseEntity<Boolean> validateToken(@Valid @RequestBody ValidationRequest request) {
        logger.debug("Token validation request received");
        
        Boolean isValid = jwtService.validateToken(request.getToken());
        
        logger.debug("Token validation result: {}", isValid);
        return ResponseEntity.ok(isValid);
    }
    
    @GetMapping("/extract-claims/{token}")
    @Operation(summary = "Extract claims from token")
    public ResponseEntity<?> extractClaims(@PathVariable String token) {
        logger.debug("Claims extraction request received");
        
        if (jwtService.validateToken(token)) {
            return ResponseEntity.ok(jwtService.extractAllClaims(token));
        }
        
        logger.warn("Invalid token provided for claims extraction");
        return ResponseEntity.badRequest().body("Invalid token");
    }
}
