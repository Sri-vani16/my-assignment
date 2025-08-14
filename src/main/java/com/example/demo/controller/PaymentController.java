package com.example.demo.controller;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.dto.RefundRequest;
import com.example.demo.exception.PaymentProcessingException;
import com.example.demo.service.PaymentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request) {
        logger.info("Received payment request for ID: {}", request.getPaymentId());
        
        // Test exception handling - throw exception for specific payment ID
        if ("TEST_ERROR".equals(request.getPaymentId())) {
            throw new PaymentProcessingException(
                "Payment processing failed for test scenario", 
                "PAYMENT_FAILED", 
                400
            );
        }
        
        PaymentResponse response = paymentService.processPayment(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refund")
    public ResponseEntity<PaymentResponse> processRefund(@Valid @RequestBody RefundRequest request) {
        logger.info("Received refund request for payment ID: {}", request.getPaymentId());
        PaymentResponse response = paymentService.processRefund(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{paymentId}/status")
    public ResponseEntity<PaymentResponse> getPaymentStatus(@PathVariable String paymentId) {
        logger.info("Received status check request for payment ID: {}", paymentId);
        PaymentResponse response = paymentService.getPaymentStatus(paymentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test-error")
    public ResponseEntity<String> testError() {
        throw new PaymentProcessingException(
            "This is a test payment error", 
            "TEST_ERROR_CODE", 
            422
        );
    }
}