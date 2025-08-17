package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.exception.PaymentProcessingException;
import com.example.demo.service.PaymentService;
import com.example.demo.service.MigrationAwarePaymentService;
import com.example.demo.service.PerformanceMonitoringService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private MigrationAwarePaymentService migrationService;
    
    @Autowired
    private PerformanceMonitoringService performanceService;


    @PostMapping("/process")
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(@Valid @RequestBody PaymentRequest request) {
        logger.info("Received payment request for ID: {}", request.getPaymentId());
        
        // Test exception handling - throw exception for specific payment ID
        if ("TEST_ERROR".equals(request.getPaymentId())) {
            throw new PaymentProcessingException(
                "Payment processing failed for test scenario", 
                "PAYMENT_FAILED", 
                400
            );
        }
        
        PaymentResponse response = migrationService.processPayment(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Payment processed successfully"));
    }

    @PostMapping("/refund")
    public ResponseEntity<ApiResponse<PaymentResponse>> processRefund(@Valid @RequestBody RefundRequest request) {
        logger.info("Received refund request for payment ID: {}", request.getPaymentId());
        PaymentResponse response = paymentService.processRefund(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Refund processed successfully"));
    }

    @GetMapping("/{paymentId}/status")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentStatus(@PathVariable String paymentId) {
        logger.info("Received status check request for payment ID: {}", paymentId);
        PaymentResponse response = paymentService.getPaymentStatus(paymentId);
        return ResponseEntity.ok(ApiResponse.success(response, "Payment status retrieved"));
    }

    @GetMapping("/test-error")
    public ResponseEntity<ApiResponse<String>> testError() {
        throw new PaymentProcessingException(
            "This is a test payment error", 
            "TEST_ERROR_CODE", 
            422
        );
    }

    @GetMapping("/test-illegal-arg")
    public ResponseEntity<ApiResponse<String>> testIllegalArg() {
        throw new IllegalArgumentException("Invalid argument provided");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<PaymentResponse>>> getPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        // Mock paginated data
        List<PaymentResponse> payments = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            payments.add(new PaymentResponse(
                "PAY_" + (page * size + i + 1),
                "SUCCESS",
                new BigDecimal("100.00"),
                "USD",
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                "Payment processed"
            ));
        }
        
        PagedResponse<PaymentResponse> pagedResponse = new PagedResponse<>(payments, page, size, 1000);
        
        return ResponseEntity.ok(ApiResponse.success(pagedResponse, "Payments retrieved successfully"));
    }
    
    @GetMapping("/performance")
    public ResponseEntity<ApiResponse<Object>> getPerformanceMetrics() {
        return ResponseEntity.ok(ApiResponse.success(performanceService.getMetrics(), "Performance metrics retrieved"));
    }
}