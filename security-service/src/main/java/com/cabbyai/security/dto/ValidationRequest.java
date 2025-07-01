package com.cabbyai.security.dto;

import jakarta.validation.constraints.NotBlank;

public class ValidationRequest {
    @NotBlank(message = "Token is required")
    private String token;
    
    public ValidationRequest() {}
    
    public ValidationRequest(String token) {
        this.token = token;
    }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
