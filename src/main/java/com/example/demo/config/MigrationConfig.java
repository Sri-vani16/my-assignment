package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "migration")
public class MigrationConfig {
    
    private int batchSize = 100;
    private int retryAttempts = 3;
    private boolean rollbackEnabled = true;
    private boolean monitoringEnabled = true;
    
    public int getBatchSize() { return batchSize; }
    public void setBatchSize(int batchSize) { this.batchSize = batchSize; }
    
    public int getRetryAttempts() { return retryAttempts; }
    public void setRetryAttempts(int retryAttempts) { this.retryAttempts = retryAttempts; }
    
    public boolean isRollbackEnabled() { return rollbackEnabled; }
    public void setRollbackEnabled(boolean rollbackEnabled) { this.rollbackEnabled = rollbackEnabled; }
    
    public boolean isMonitoringEnabled() { return monitoringEnabled; }
    public void setMonitoringEnabled(boolean monitoringEnabled) { this.monitoringEnabled = monitoringEnabled; }
}