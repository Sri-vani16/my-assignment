package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.PerformanceMetrics;
import com.example.demo.service.PerformanceMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/monitoring")
public class MonitoringController {

    @Autowired
    private PerformanceMonitoringService performanceService;
    
    @Autowired
    private CacheManager cacheManager;

    @GetMapping("/metrics")
    public ResponseEntity<ApiResponse<PerformanceMetrics>> getMetrics() {
        PerformanceMetrics metrics = performanceService.getMetrics();
        return ResponseEntity.ok(ApiResponse.success(metrics, "Performance metrics retrieved"));
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, String>>> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(ApiResponse.success(health, "Application is healthy"));
    }

    @PostMapping("/cache/clear")
    public ResponseEntity<ApiResponse<String>> clearCache(@RequestParam(required = false) String cacheName) {
        if (cacheName != null) {
            cacheManager.getCache(cacheName).clear();
            return ResponseEntity.ok(ApiResponse.success("Cache cleared", "Cache '" + cacheName + "' cleared successfully"));
        } else {
            cacheManager.getCacheNames().forEach(name -> cacheManager.getCache(name).clear());
            return ResponseEntity.ok(ApiResponse.success("All caches cleared", "All caches cleared successfully"));
        }
    }
}