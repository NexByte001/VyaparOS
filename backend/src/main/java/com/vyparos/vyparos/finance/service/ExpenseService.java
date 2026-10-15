package com.vyparos.vyparos.finance.service;

import com.vyparos.vyparos.common.dto.PageResponse;
import com.vyparos.vyparos.finance.dto.ExpenseRequest;
import com.vyparos.vyparos.finance.dto.ExpenseResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ExpenseService {
    PageResponse<ExpenseResponse> getExpenses(Long businessId, String category, LocalDate startDate, LocalDate endDate, Pageable pageable);
    ExpenseResponse getExpenseById(Long businessId, Long expenseId);
    ExpenseResponse createExpense(Long businessId, Long userId, ExpenseRequest request);
    ExpenseResponse updateExpense(Long businessId, Long expenseId, ExpenseRequest request);
    void deleteExpense(Long businessId, Long expenseId);
}
