package com.example.demo.service;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.dto.RefundRequest;
import com.example.demo.exception.PaymentProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private final AtomicInteger requestCounter = new AtomicInteger(0);
    
    @Autowired
    private WebClient paymentWebClient;

    public PaymentResponse processPayment(PaymentRequest request) {
        int requestId = requestCounter.incrementAndGet();
        logger.info("[REQ-{}] Processing payment for ID: {}, Amount: {}, Currency: {}", 
                   requestId, request.getPaymentId(), request.getAmount(), request.getCurrency());
        
        try {
            return paymentWebClient
                    .post()
                    .uri("/payments")
                    .header("Authorization", "Bearer <api-key>")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(PaymentResponse.class)
                    .retryWhen(Retry.backoff(MAX_RETRY_ATTEMPTS, Duration.ofSeconds(1))
                            .filter(this::isRetryableException))
                    .doOnSuccess(response -> logger.info("[REQ-{}] Payment processed successfully: {} - Status: {}, Transaction: {}", 
                                                       requestId, response.getPaymentId(), response.getStatus(), response.getTransactionId()))
                    .doOnError(error -> logger.error("[REQ-{}] Payment processing failed for ID: {}", requestId, request.getPaymentId(), error))
                    .onErrorMap(this::mapToPaymentException)
                    .block();
        } catch (Exception e) {
            logger.error("[REQ-{}] Unexpected error processing payment: {}", requestId, request.getPaymentId(), e);
            throw new PaymentProcessingException("Payment processing failed", "PROCESSING_ERROR", 500, e);
        }
    }

    public PaymentResponse processRefund(RefundRequest request) {
        int requestId = requestCounter.incrementAndGet();
        logger.info("[REQ-{}] Processing refund for payment ID: {}, Amount: {}", 
                   requestId, request.getPaymentId(), request.getAmount());
        
        try {
            return paymentWebClient
                    .post()
                    .uri("/refunds")
                    .header("Authorization", "Bearer <api-key>")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(PaymentResponse.class)
                    .retryWhen(Retry.backoff(MAX_RETRY_ATTEMPTS, Duration.ofSeconds(1))
                            .filter(this::isRetryableException))
                    .doOnSuccess(response -> logger.info("[REQ-{}] Refund processed successfully: {} - Status: {}, Transaction: {}", 
                                                       requestId, response.getPaymentId(), response.getStatus(), response.getTransactionId()))
                    .doOnError(error -> logger.error("[REQ-{}] Refund processing failed for ID: {}", requestId, request.getPaymentId(), error))
                    .onErrorMap(this::mapToPaymentException)
                    .block();
        } catch (Exception e) {
            logger.error("[REQ-{}] Unexpected error processing refund: {}", requestId, request.getPaymentId(), e);
            throw new PaymentProcessingException("Refund processing failed", "REFUND_ERROR", 500, e);
        }
    }

    public PaymentResponse getPaymentStatus(String paymentId) {
        int requestId = requestCounter.incrementAndGet();
        logger.info("[REQ-{}] Checking payment status for ID: {}", requestId, paymentId);
        
        try {
            return paymentWebClient
                    .get()
                    .uri("/payments/{paymentId}/status", paymentId)
                    .header("Authorization", "Bearer <api-key>")
                    .retrieve()
                    .bodyToMono(PaymentResponse.class)
                    .retryWhen(Retry.backoff(MAX_RETRY_ATTEMPTS, Duration.ofSeconds(1))
                            .filter(this::isRetryableException))
                    .doOnSuccess(response -> logger.info("[REQ-{}] Payment status retrieved: {} - Status: {}, Last Updated: {}", 
                                                       requestId, paymentId, response.getStatus(), response.getProcessedAt()))
                    .doOnError(error -> logger.error("[REQ-{}] Failed to get payment status for ID: {}", requestId, paymentId, error))
                    .onErrorResume(error -> {
                        logger.warn("[REQ-{}] External API unavailable, returning mock status for: {}", requestId, paymentId);
                        return Mono.just(createMockStatusResponse(paymentId));
                    })
                    .block();
        } catch (Exception e) {
            logger.error("[REQ-{}] Unexpected error getting payment status: {}", requestId, paymentId, e);
            return createMockStatusResponse(paymentId);
        }
    }

    private boolean isRetryableException(Throwable throwable) {
        if (throwable instanceof WebClientResponseException) {
            WebClientResponseException ex = (WebClientResponseException) throwable;
            return ex.getStatusCode().is5xxServerError() || 
                   ex.getStatusCode() == HttpStatus.REQUEST_TIMEOUT ||
                   ex.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS;
        }
        return throwable instanceof java.net.ConnectException ||
               throwable instanceof java.util.concurrent.TimeoutException;
    }

    private PaymentProcessingException mapToPaymentException(Throwable throwable) {
        if (throwable instanceof WebClientResponseException) {
            WebClientResponseException ex = (WebClientResponseException) throwable;
            String errorCode = "API_ERROR_" + ex.getStatusCode().value();
            return new PaymentProcessingException(
                "External API error: " + ex.getResponseBodyAsString(),
                errorCode,
                ex.getStatusCode().value(),
                ex
            );
        }
        
        if (throwable instanceof java.util.concurrent.TimeoutException) {
            return new PaymentProcessingException("Request timeout", "TIMEOUT_ERROR", 408, throwable);
        }
        
        return new PaymentProcessingException("Unknown error", "UNKNOWN_ERROR", 500, throwable);
    }

    private PaymentResponse createMockStatusResponse(String paymentId) {
        return new PaymentResponse(
            paymentId,
            "SUCCESS",
            new BigDecimal("100.00"),
            "USD",
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            "Payment status retrieved (mock)"
        );
    }

    // Mock method for demonstration - simulates external API response
    public PaymentResponse mockProcessPayment(PaymentRequest request) {
        logger.info("Mock processing payment for ID: {}", request.getPaymentId());
        
        return new PaymentResponse(
            request.getPaymentId(),
            "SUCCESS",
            request.getAmount(),
            request.getCurrency(),
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            "Payment processed successfully"
        );
    }
}