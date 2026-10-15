package com.vyparos.vyparos.billing.dto;

import com.vyparos.vyparos.billing.entity.Invoice;
import com.vyparos.vyparos.billing.entity.InvoiceStatus;
import com.vyparos.vyparos.customers.dto.CustomerResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class InvoiceDetailResponse {

    private Long id;
    private Long businessId;
    private String invoiceNumber;
    private CustomerResponse customer;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal balanceDue;
    private InvoiceStatus status;
    private String notes;
    private List<InvoiceItemResponse> items;
    private List<PaymentResponse> payments;
    private Instant createdAt;
    private Instant updatedAt;

    public InvoiceDetailResponse() {}

    public static InvoiceDetailResponse fromEntity(Invoice invoice) {
        InvoiceDetailResponse response = new InvoiceDetailResponse();
        response.setId(invoice.getId());
        response.setBusinessId(invoice.getBusiness().getId());
        response.setInvoiceNumber(invoice.getInvoiceNumber());
        if (invoice.getCustomer() != null) {
            response.setCustomer(CustomerResponse.fromEntity(invoice.getCustomer()));
        }
        response.setInvoiceDate(invoice.getInvoiceDate());
        response.setDueDate(invoice.getDueDate());
        response.setSubtotal(invoice.getSubtotal());
        response.setDiscountAmount(invoice.getDiscountAmount());
        response.setTaxAmount(invoice.getTaxAmount());
        response.setTotalAmount(invoice.getTotalAmount());
        response.setPaidAmount(invoice.getPaidAmount());
        response.setBalanceDue(invoice.getBalanceDue());
        response.setStatus(invoice.getStatus());
        response.setNotes(invoice.getNotes());
        response.setItems(invoice.getItems().stream().map(InvoiceItemResponse::fromEntity).toList());
        response.setPayments(invoice.getPayments().stream().map(PaymentResponse::fromEntity).toList());
        response.setCreatedAt(invoice.getCreatedAt());
        response.setUpdatedAt(invoice.getUpdatedAt());
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

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public CustomerResponse getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerResponse customer) {
        this.customer = customer;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getBalanceDue() {
        return balanceDue;
    }

    public void setBalanceDue(BigDecimal balanceDue) {
        this.balanceDue = balanceDue;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<InvoiceItemResponse> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItemResponse> items) {
        this.items = items;
    }

    public List<PaymentResponse> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentResponse> payments) {
        this.payments = payments;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
