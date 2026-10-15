package com.vyparos.vyparos.finance.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FinancialReportResponse {

    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal invoicedRevenue;
    private BigDecimal cashCollected;
    private BigDecimal recordedExpenses;
    private BigDecimal operatingProfit; // Cash Collected - Recorded Expenses
    private BigDecimal pendingReceivables; // Total unpaid balance
    private List<CategoryExpenseSummaryDto> expenseBreakdown;

    public FinancialReportResponse() {}

    public FinancialReportResponse(LocalDate startDate, LocalDate endDate, BigDecimal invoicedRevenue, BigDecimal cashCollected, BigDecimal recordedExpenses, BigDecimal operatingProfit, BigDecimal pendingReceivables, List<CategoryExpenseSummaryDto> expenseBreakdown) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.invoicedRevenue = invoicedRevenue;
        this.cashCollected = cashCollected;
        this.recordedExpenses = recordedExpenses;
        this.operatingProfit = operatingProfit;
        this.pendingReceivables = pendingReceivables;
        this.expenseBreakdown = expenseBreakdown;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getInvoicedRevenue() {
        return invoicedRevenue;
    }

    public void setInvoicedRevenue(BigDecimal invoicedRevenue) {
        this.invoicedRevenue = invoicedRevenue;
    }

    public BigDecimal getCashCollected() {
        return cashCollected;
    }

    public void setCashCollected(BigDecimal cashCollected) {
        this.cashCollected = cashCollected;
    }

    public BigDecimal getRecordedExpenses() {
        return recordedExpenses;
    }

    public void setRecordedExpenses(BigDecimal recordedExpenses) {
        this.recordedExpenses = recordedExpenses;
    }

    public BigDecimal getOperatingProfit() {
        return operatingProfit;
    }

    public void setOperatingProfit(BigDecimal operatingProfit) {
        this.operatingProfit = operatingProfit;
    }

    public BigDecimal getPendingReceivables() {
        return pendingReceivables;
    }

    public void setPendingReceivables(BigDecimal pendingReceivables) {
        this.pendingReceivables = pendingReceivables;
    }

    public List<CategoryExpenseSummaryDto> getExpenseBreakdown() {
        return expenseBreakdown;
    }

    public void setExpenseBreakdown(List<CategoryExpenseSummaryDto> expenseBreakdown) {
        this.expenseBreakdown = expenseBreakdown;
    }
}
