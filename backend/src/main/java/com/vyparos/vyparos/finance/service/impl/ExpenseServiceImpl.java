package com.vyparos.vyparos.finance.service.impl;

import com.vyparos.vyparos.business.entity.BusinessMembership;
import com.vyparos.vyparos.common.dto.PageResponse;
import com.vyparos.vyparos.exception.ResourceNotFoundException;
import com.vyparos.vyparos.finance.dto.ExpenseRequest;
import com.vyparos.vyparos.finance.dto.ExpenseResponse;
import com.vyparos.vyparos.finance.entity.Expense;
import com.vyparos.vyparos.finance.repository.ExpenseRepository;
import com.vyparos.vyparos.finance.service.ExpenseService;
import com.vyparos.vyparos.identity.entity.User;
import com.vyparos.vyparos.identity.repository.UserRepository;
import com.vyparos.vyparos.security.TenantSecurityGuard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final TenantSecurityGuard securityGuard;

    public ExpenseServiceImpl(
            ExpenseRepository expenseRepository,
            UserRepository userRepository,
            TenantSecurityGuard securityGuard) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.securityGuard = securityGuard;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ExpenseResponse> getExpenses(Long businessId, String category, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        securityGuard.requireAdminOrOwner(businessId);
        Page<Expense> expensePage = expenseRepository.findFilteredExpenses(businessId, category, startDate, endDate, pageable);
        return PageResponse.from(expensePage.map(ExpenseResponse::fromEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponse getExpenseById(Long businessId, Long expenseId) {
        securityGuard.requireAdminOrOwner(businessId);
        Expense expense = expenseRepository.findByIdAndBusinessId(expenseId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found in this business workspace"));
        return ExpenseResponse.fromEntity(expense);
    }

    @Override
    @Transactional
    public ExpenseResponse createExpense(Long businessId, Long userId, ExpenseRequest request) {
        BusinessMembership adminMembership = securityGuard.requireAdminOrOwner(businessId);
        User user = userRepository.findById(userId).orElse(null);

        Expense expense = new Expense(
                adminMembership.getBusiness(),
                request.getCategory().trim(),
                request.getDescription() != null ? request.getDescription().trim() : null,
                request.getAmount(),
                request.getExpenseDate(),
                request.getPaymentMethod(),
                request.getVendor() != null ? request.getVendor().trim() : null,
                user
        );

        Expense saved = expenseRepository.save(expense);
        return ExpenseResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public ExpenseResponse updateExpense(Long businessId, Long expenseId, ExpenseRequest request) {
        securityGuard.requireAdminOrOwner(businessId);

        Expense expense = expenseRepository.findByIdAndBusinessId(expenseId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found in this business workspace"));

        if (request.getCategory() != null && !request.getCategory().isBlank()) {
            expense.setCategory(request.getCategory().trim());
        }
        if (request.getDescription() != null) {
            expense.setDescription(request.getDescription().trim());
        }
        if (request.getAmount() != null) {
            expense.setAmount(request.getAmount());
        }
        if (request.getExpenseDate() != null) {
            expense.setExpenseDate(request.getExpenseDate());
        }
        if (request.getPaymentMethod() != null) {
            expense.setPaymentMethod(request.getPaymentMethod());
        }
        if (request.getVendor() != null) {
            expense.setVendor(request.getVendor().trim());
        }

        Expense saved = expenseRepository.save(expense);
        return ExpenseResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public void deleteExpense(Long businessId, Long expenseId) {
        securityGuard.requireAdminOrOwner(businessId);

        Expense expense = expenseRepository.findByIdAndBusinessId(expenseId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found in this business workspace"));

        expenseRepository.delete(expense);
    }
}
