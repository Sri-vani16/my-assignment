package com.example.demo.service;

import com.example.demo.dto.PerformanceMetrics;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PerformanceMonitoringService {
    private final CacheManager cacheManager;
    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong totalResponseTime = new AtomicLong(0);
    private final Map<String, AtomicLong> endpointCounters = new HashMap<>();

    public PerformanceMonitoringService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void recordRequest(String endpoint, long responseTime) {
        totalRequests.incrementAndGet();
        totalResponseTime.addAndGet(responseTime);
        endpointCounters.computeIfAbsent(endpoint, k -> new AtomicLong(0)).incrementAndGet();
    }

    public PerformanceMetrics getMetrics() {
        PerformanceMetrics metrics = new PerformanceMetrics();
        
        long requests = totalRequests.get();
        metrics.setTotalRequests(requests);
        metrics.setAverageResponseTime(requests > 0 ? (double) totalResponseTime.get() / requests : 0);
        
        // Endpoint statistics
        Map<String, Long> endpointStats = new HashMap<>();
        endpointCounters.forEach((endpoint, counter) -> endpointStats.put(endpoint, counter.get()));
        metrics.setEndpointStats(endpointStats);
        
        // Memory usage
        Runtime runtime = Runtime.getRuntime();
        metrics.setMemoryUsed(runtime.totalMemory() - runtime.freeMemory());
        metrics.setMemoryTotal(runtime.totalMemory());
        
        return metrics;
    }
}