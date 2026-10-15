package com.vyparos.vyparos.finance.dto;

import com.vyparos.vyparos.finance.entity.Expense;
import com.vyparos.vyparos.identity.dto.UserSummaryDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class ExpenseResponse {

    private Long id;
    private Long businessId;
    private String category;
    private String description;
    private BigDecimal amount;
    private LocalDate expenseDate;
    private String paymentMethod;
    private String vendor;
    private UserSummaryDto createdBy;
    private Instant createdAt;
    private Instant updatedAt;

    public ExpenseResponse() {}

    public static ExpenseResponse fromEntity(Expense expense) {
        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setBusinessId(expense.getBusiness().getId());
        response.setCategory(expense.getCategory());
        response.setDescription(expense.getDescription());
        response.setAmount(expense.getAmount());
        response.setExpenseDate(expense.getExpenseDate());
        response.setPaymentMethod(expense.getPaymentMethod());
        response.setVendor(expense.getVendor());
        if (expense.getCreatedByUser() != null) {
            response.setCreatedBy(UserSummaryDto.fromEntity(expense.getCreatedByUser()));
        }
        response.setCreatedAt(expense.getCreatedAt());
        response.setUpdatedAt(expense.getUpdatedAt());
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public UserSummaryDto getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserSummaryDto createdBy) {
        this.createdBy = createdBy;
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
