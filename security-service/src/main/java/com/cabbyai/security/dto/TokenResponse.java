package com.cabbyai.security.dto;

public class TokenResponse {
    private String token;
    private String type;
    private Long expiresIn;
    
    public TokenResponse(String token, String type, Long expiresIn) {
        this.token = token;
        this.type = type;
        this.expiresIn = expiresIn;
    }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(Long expiresIn) { this.expiresIn = expiresIn; }
}
