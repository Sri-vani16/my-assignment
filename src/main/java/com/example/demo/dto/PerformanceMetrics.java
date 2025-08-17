package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class PerformanceMetrics {
    private LocalDateTime timestamp;
    private long totalRequests;
    private double averageResponseTime;
    private long cacheHits;
    private long cacheMisses;
    private double cacheHitRatio;
    private Map<String, Long> endpointStats;
    private long memoryUsed;
    private long memoryTotal;

    public PerformanceMetrics() {
        this.timestamp = LocalDateTime.now();
    }

    // Getters and setters
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public long getTotalRequests() { return totalRequests; }
    public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }
    public double getAverageResponseTime() { return averageResponseTime; }
    public void setAverageResponseTime(double averageResponseTime) { this.averageResponseTime = averageResponseTime; }
    public long getCacheHits() { return cacheHits; }
    public void setCacheHits(long cacheHits) { this.cacheHits = cacheHits; }
    public long getCacheMisses() { return cacheMisses; }
    public void setCacheMisses(long cacheMisses) { this.cacheMisses = cacheMisses; }
    public double getCacheHitRatio() { return cacheHitRatio; }
    public void setCacheHitRatio(double cacheHitRatio) { this.cacheHitRatio = cacheHitRatio; }
    public Map<String, Long> getEndpointStats() { return endpointStats; }
    public void setEndpointStats(Map<String, Long> endpointStats) { this.endpointStats = endpointStats; }
    public long getMemoryUsed() { return memoryUsed; }
    public void setMemoryUsed(long memoryUsed) { this.memoryUsed = memoryUsed; }
    public long getMemoryTotal() { return memoryTotal; }
    public void setMemoryTotal(long memoryTotal) { this.memoryTotal = memoryTotal; }
}