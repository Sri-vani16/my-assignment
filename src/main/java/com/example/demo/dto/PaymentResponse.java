package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {
    private String paymentId;
    private String status;
    private BigDecimal amount;
    private String currency;
    private String transactionId;
    private LocalDateTime processedAt;
    private String message;

    public PaymentResponse() {}

    public PaymentResponse(String paymentId, String status, BigDecimal amount, String currency, 
                          String transactionId, LocalDateTime processedAt, String message) {
        this.paymentId = paymentId;
        this.status = status;
        this.amount = amount;
        this.currency = currency;
        this.transactionId = transactionId;
        this.processedAt = processedAt;
        this.message = message;
    }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}