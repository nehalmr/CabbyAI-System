package com.cabbyai.user.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {
    private String errorCode;
    private String message;
    private Map<String, String> details;
    private LocalDateTime timestamp;
    private String service;
    
    public ErrorResponse(String errorCode, String message, Map<String, String> details, LocalDateTime timestamp) {
        this.errorCode = errorCode;
        this.message = message;
        this.details = details;
        this.timestamp = timestamp;
        this.service = "user-service";
    }
    
    // Getters and setters
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Map<String, String> getDetails() { return details; }
    public void setDetails(Map<String, String> details) { this.details = details; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
}
