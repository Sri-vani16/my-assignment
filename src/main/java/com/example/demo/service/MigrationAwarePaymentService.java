package com.example.demo.service;

import com.example.demo.config.FeatureFlagConfig;
import com.example.demo.dto.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MigrationAwarePaymentService {
    
    private static final Logger logger = LoggerFactory.getLogger(MigrationAwarePaymentService.class);
    
    @Autowired
    private FeatureFlagConfig featureFlagConfig;
    
    @Autowired
    private PaymentService newPaymentService;
    
    @Autowired
    private LegacyPaymentService legacyPaymentService;
    
    @Autowired
    private MigrationService migrationService;
    
    @CircuitBreaker(name = "legacy-api", fallbackMethod = "fallbackPayment")
    public PaymentResponse processPayment(PaymentRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            boolean useNewApi = shouldUseNewPaymentApi(request.getUserId());
            PaymentResponse response;
            
            if (useNewApi) {
                logger.info("Processing payment {} with NEW API", request.getPaymentId());
                response = newPaymentService.processPayment(request);
            } else {
                logger.info("Processing payment {} with LEGACY API", request.getPaymentId());
                response = legacyPaymentService.processPaymentLegacy(request);
            }
            
            long responseTime = System.currentTimeMillis() - startTime;
            migrationService.logMigrationMetrics("payment", useNewApi, responseTime);
            
            return response;
            
        } catch (Exception e) {
            logger.error("Payment processing failed for {}: {}", request.getPaymentId(), e.getMessage());
            throw e;
        }
    }
    
    public PaymentResponse fallbackPayment(PaymentRequest request, Exception ex) {
        logger.warn("Falling back to legacy payment for {}: {}", request.getPaymentId(), ex.getMessage());
        return legacyPaymentService.processPaymentLegacy(request);
    }
    
    private boolean shouldUseNewPaymentApi(String userId) {
        return featureFlagConfig.isEnableNewPaymentApi() && 
               migrationService.shouldUseNewApi(userId);
    }
}