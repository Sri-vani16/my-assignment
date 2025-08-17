package com.example.demo.service;

import com.example.demo.dto.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class LegacyPaymentService {
    
    public PaymentResponse processPaymentLegacy(PaymentRequest request) {
        // Legacy payment processing logic
        return new PaymentResponse(
            request.getPaymentId(),
            "LEGACY_SUCCESS",
            request.getAmount(),
            request.getCurrency(),
            "LEGACY_TXN_" + System.currentTimeMillis(),
            LocalDateTime.now(),
            "Processed via legacy system"
        );
    }
    
    public PaymentResponse processRefundLegacy(RefundRequest request) {
        return new PaymentResponse(
            request.getPaymentId(),
            "LEGACY_REFUNDED",
            request.getAmount(),
            "USD",
            "LEGACY_REF_" + System.currentTimeMillis(),
            LocalDateTime.now(),
            "Refunded via legacy system"
        );
    }
    
    public PaymentResponse getPaymentStatusLegacy(String paymentId) {
        return new PaymentResponse(
            paymentId,
            "LEGACY_SUCCESS",
            new BigDecimal("100.00"),
            "USD",
            "LEGACY_TXN_" + System.currentTimeMillis(),
            LocalDateTime.now(),
            "Status retrieved via legacy system"
        );
    }
}