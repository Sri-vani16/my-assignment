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

    public PaymentRequest() {}

    public PaymentRequest(String paymentId, BigDecimal amount, String currency, String customerId) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.currency = currency;
        this.customerId = customerId;
    }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
}