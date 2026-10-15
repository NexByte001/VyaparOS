package com.vyparos.vyparos.billing.dto;

import com.vyparos.vyparos.billing.entity.Payment;
import com.vyparos.vyparos.billing.entity.PaymentMethod;
import com.vyparos.vyparos.identity.dto.UserSummaryDto;

import java.math.BigDecimal;
import java.time.Instant;

public class PaymentResponse {

    private Long id;
    private Long businessId;
    private Long invoiceId;
    private BigDecimal amount;
    private Instant paymentDate;
    private PaymentMethod paymentMethod;
    private String transactionReference;
    private UserSummaryDto recordedBy;
    private String notes;
    private Instant createdAt;

    public PaymentResponse() {}

    public static PaymentResponse fromEntity(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setBusinessId(payment.getBusiness().getId());
        response.setInvoiceId(payment.getInvoice().getId());
        response.setAmount(payment.getAmount());
        response.setPaymentDate(payment.getPaymentDate());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setTransactionReference(payment.getTransactionReference());
        if (payment.getRecordedByUser() != null) {
            response.setRecordedBy(UserSummaryDto.fromEntity(payment.getRecordedByUser()));
        }
        response.setNotes(payment.getNotes());
        response.setCreatedAt(payment.getCreatedAt());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public UserSummaryDto getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(UserSummaryDto recordedBy) {
        this.recordedBy = recordedBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
