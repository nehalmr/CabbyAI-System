package com.cabbyai.user.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class SecurityClientFallback implements SecurityClient {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityClientFallback.class);
    
    @Override
    public Map<String, Object> generateToken(Map<String, Object> request) {
        logger.error("Security service is unavailable - fallback triggered");
        throw new RuntimeException("Authentication service is temporarily unavailable");
    }
}
