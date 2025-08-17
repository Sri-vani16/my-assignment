package com.example.demo.service;

import com.example.demo.config.MigrationConfig;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class MigrationMonitoringService {
    
    private static final Logger logger = LoggerFactory.getLogger(MigrationMonitoringService.class);
    
    @Autowired
    private MigrationConfig migrationConfig;
    
    private final MeterRegistry meterRegistry;
    private final Map<String, Counter> apiUsageCounters = new ConcurrentHashMap<>();
    private final Map<String, Timer> responseTimeTimers = new ConcurrentHashMap<>();
    
    public MigrationMonitoringService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }
    
    public void recordApiUsage(String apiType, String operation) {
        if (!migrationConfig.isMonitoringEnabled()) return;
        
        String key = apiType + "." + operation;
        Counter counter = apiUsageCounters.computeIfAbsent(key, 
            k -> Counter.builder("migration.api.usage")
                       .tag("api", apiType)
                       .tag("operation", operation)
                       .register(meterRegistry));
        counter.increment();
        
        logger.debug("Recorded API usage: {} - {}", apiType, operation);
    }
    
    public Timer.Sample startTimer(String operation) {
        if (!migrationConfig.isMonitoringEnabled()) return null;
        
        Timer timer = responseTimeTimers.computeIfAbsent(operation,
            k -> Timer.builder("migration.response.time")
                     .tag("operation", k)
                     .register(meterRegistry));
        return Timer.start(meterRegistry);
    }
    
    public void recordResponseTime(Timer.Sample sample, String operation) {
        if (sample != null && migrationConfig.isMonitoringEnabled()) {
            Timer timer = responseTimeTimers.get(operation);
            if (timer != null) {
                sample.stop(timer);
            }
        }
    }
    
    public void recordMigrationError(String apiType, String errorType) {
        if (!migrationConfig.isMonitoringEnabled()) return;
        
        Counter.builder("migration.errors")
               .tag("api", apiType)
               .tag("error", errorType)
               .register(meterRegistry)
               .increment();
        
        logger.warn("Migration error recorded: {} - {}", apiType, errorType);
    }
}