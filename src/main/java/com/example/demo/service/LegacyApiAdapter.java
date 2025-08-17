package com.example.demo.service;

import com.example.demo.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LegacyApiAdapter {
    
    private static final Logger logger = LoggerFactory.getLogger(LegacyApiAdapter.class);
    
    public PaymentResponse adaptLegacyPaymentResponse(Object legacyResponse) {
        logger.debug("Adapting legacy payment response to new format");
        
        if (legacyResponse instanceof PaymentResponse) {
            PaymentResponse legacy = (PaymentResponse) legacyResponse;
            return new PaymentResponse(
                legacy.getPaymentId(),
                mapLegacyStatus(legacy.getStatus()),
                legacy.getAmount(),
                legacy.getCurrency(),
                legacy.getTransactionId(),
                legacy.getProcessedAt(),
                "Adapted from legacy: " + legacy.getMessage()
            );
        }
        
        throw new IllegalArgumentException("Unsupported legacy response type");
    }
    
    public PaymentRequest adaptToLegacyPaymentRequest(PaymentRequest newRequest) {
        logger.debug("Adapting new payment request to legacy format");
        
        return new PaymentRequest(
            newRequest.getPaymentId(),
            newRequest.getUserId(),
            newRequest.getAmount(),
            newRequest.getCurrency(),
            newRequest.getPaymentMethod(),
            newRequest.getDescription()
        );
    }
    
    private String mapLegacyStatus(String legacyStatus) {
        switch (legacyStatus) {
            case "LEGACY_SUCCESS": return "SUCCESS";
            case "LEGACY_FAILED": return "FAILED";
            case "LEGACY_PENDING": return "PENDING";
            case "LEGACY_REFUNDED": return "REFUNDED";
            default: return legacyStatus;
        }
    }
}