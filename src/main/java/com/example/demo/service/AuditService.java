package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuditService {
    
    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");
    private final Map<String, Integer> loginAttempts = new ConcurrentHashMap<>();
    
    public void logLoginAttempt(String username, String ipAddress, boolean success) {
        String event = success ? "LOGIN_SUCCESS" : "LOGIN_FAILED";
        auditLogger.info("EVENT={} | USER={} | IP={} | TIME={}", 
            event, username, ipAddress, LocalDateTime.now());
        
        if (!success) {
            loginAttempts.merge(username, 1, Integer::sum);
        } else {
            loginAttempts.remove(username);
        }
    }
    
    public void logTokenValidation(String username, String ipAddress, boolean success) {
        String event = success ? "TOKEN_VALID" : "TOKEN_INVALID";
        auditLogger.info("EVENT={} | USER={} | IP={} | TIME={}", 
            event, username, ipAddress, LocalDateTime.now());
    }
    
    public void logSecurityViolation(String username, String ipAddress, String violation) {
        auditLogger.warn("EVENT=SECURITY_VIOLATION | USER={} | IP={} | VIOLATION={} | TIME={}", 
            username, ipAddress, violation, LocalDateTime.now());
    }
    
    public boolean isAccountLocked(String username) {
        return loginAttempts.getOrDefault(username, 0) >= 5;
    }
    
    public void resetLoginAttempts(String username) {
        loginAttempts.remove(username);
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}