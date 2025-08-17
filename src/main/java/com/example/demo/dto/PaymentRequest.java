package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class PaymentRequest {
    @NotNull
    private String paymentId;
    
    @NotNull
    @Positive
    private BigDecimal amount;
    
    @NotNull
    private String currency;
    
    @NotNull
    private String customerId;
    
    private String paymentMethod;
    private String description;

    public PaymentRequest() {}

    public PaymentRequest(String paymentId, BigDecimal amount, String currency, String customerId) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.currency = currency;
        this.customerId = customerId;
    }
    
    public PaymentRequest(String paymentId, String userId, BigDecimal amount, String currency, String paymentMethod, String description) {
        this.paymentId = paymentId;
        this.customerId = userId;
        this.amount = amount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.description = description;
    }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    
    public String getUserId() { return customerId; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}