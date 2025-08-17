package com.example.demo.service;

import com.example.demo.config.FeatureFlagConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MigrationService {
    
    private static final Logger logger = LoggerFactory.getLogger(MigrationService.class);
    
    @Autowired
    private FeatureFlagConfig featureFlagConfig;
    
    private final Random random = new Random();
    
    /**
     * Determines if a user should use the new API based on migration percentage
     */
    public boolean shouldUseNewApi(String userId) {
        if (!featureFlagConfig.isEnableLegacySupport()) {
            return true; // Force new API if legacy support is disabled
        }
        
        double migrationPercentage = featureFlagConfig.getMigrationPercentage();
        if (migrationPercentage >= 100.0) {
            return true;
        }
        
        // Use consistent hash-based routing for same user
        int userHash = Math.abs(userId.hashCode() % 100);
        boolean useNew = userHash < migrationPercentage;
        
        logger.debug("User {} (hash: {}) will use {} API (migration: {}%)", 
                    userId, userHash, useNew ? "NEW" : "LEGACY", migrationPercentage);
        
        return useNew;
    }
    
    /**
     * Check if specific feature is enabled
     */
    public boolean isFeatureEnabled(String featureName) {
        switch (featureName.toLowerCase()) {
            case "payment":
                return featureFlagConfig.isEnableNewPaymentApi();
            case "auth":
                return featureFlagConfig.isEnableNewAuthSystem();
            case "user":
                return featureFlagConfig.isEnableNewUserManagement();
            case "cache":
                return featureFlagConfig.isEnableNewCaching();
            default:
                return false;
        }
    }
    
    /**
     * Log migration metrics
     */
    public void logMigrationMetrics(String operation, boolean usedNewApi, long responseTime) {
        logger.info("Migration Metrics - Operation: {}, API: {}, ResponseTime: {}ms", 
                   operation, usedNewApi ? "NEW" : "LEGACY", responseTime);
    }
}