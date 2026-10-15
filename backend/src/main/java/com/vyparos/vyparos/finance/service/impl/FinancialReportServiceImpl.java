package com.vyparos.vyparos.finance.service.impl;

import com.vyparos.vyparos.billing.repository.InvoiceRepository;
import com.vyparos.vyparos.billing.repository.PaymentRepository;
import com.vyparos.vyparos.finance.dto.CategoryExpenseSummaryDto;
import com.vyparos.vyparos.finance.dto.FinancialReportResponse;
import com.vyparos.vyparos.finance.repository.ExpenseRepository;
import com.vyparos.vyparos.finance.service.FinancialReportService;
import com.vyparos.vyparos.security.TenantSecurityGuard;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
public class FinancialReportServiceImpl implements FinancialReportService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final ExpenseRepository expenseRepository;
    private final TenantSecurityGuard securityGuard;

    public FinancialReportServiceImpl(
            InvoiceRepository invoiceRepository,
            PaymentRepository paymentRepository,
            ExpenseRepository expenseRepository,
            TenantSecurityGuard securityGuard) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.expenseRepository = expenseRepository;
        this.securityGuard = securityGuard;
    }

    @Override
    @Transactional(readOnly = true)
    public FinancialReportResponse getFinancialSummary(Long businessId, LocalDate startDate, LocalDate endDate) {
        securityGuard.requireAdminOrOwner(businessId);

        LocalDate start = startDate != null ? startDate : LocalDate.now().minusMonths(1);
        LocalDate end = endDate != null ? endDate : LocalDate.now();

        BigDecimal invoicedRevenue = invoiceRepository.sumInvoicedRevenue(businessId, start, end);
        BigDecimal cashCollected = paymentRepository.sumPaymentsCollected(
                businessId,
                start.atStartOfDay().toInstant(ZoneOffset.UTC),
                end.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)
        );
        BigDecimal recordedExpenses = expenseRepository.sumExpenses(businessId, start, end);
        BigDecimal operatingProfit = cashCollected.subtract(recordedExpenses);
        BigDecimal pendingReceivables = invoiceRepository.sumPendingBalance(businessId);

        List<Object[]> categorySums = expenseRepository.sumExpensesByCategory(businessId, start, end);
        List<CategoryExpenseSummaryDto> breakdown = new ArrayList<>();
        for (Object[] row : categorySums) {
            String category = (String) row[0];
            BigDecimal sum = (BigDecimal) row[1];
            breakdown.add(new CategoryExpenseSummaryDto(category, sum));
        }

        return new FinancialReportResponse(
                start,
                end,
                invoicedRevenue,
                cashCollected,
                recordedExpenses,
                operatingProfit,
                pendingReceivables,
                breakdown
        );
    }
}
