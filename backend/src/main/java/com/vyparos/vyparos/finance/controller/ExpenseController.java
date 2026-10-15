package com.vyparos.vyparos.finance.controller;

import com.vyparos.vyparos.common.dto.ApiResponse;
import com.vyparos.vyparos.common.dto.PageResponse;
import com.vyparos.vyparos.finance.dto.ExpenseRequest;
import com.vyparos.vyparos.finance.dto.ExpenseResponse;
import com.vyparos.vyparos.finance.service.ExpenseService;
import com.vyparos.vyparos.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/businesses/{businessId}/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ExpenseResponse>>> getExpenses(
            @PathVariable Long businessId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 20, sort = "expenseDate", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<ExpenseResponse> response = expenseService.getExpenses(businessId, category, startDate, endDate, pageable);
        return ResponseEntity.ok(ApiResponse.ok("Expenses retrieved successfully", response));
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> getExpenseById(
            @PathVariable Long businessId,
            @PathVariable Long expenseId) {
        ExpenseResponse response = expenseService.getExpenseById(businessId, expenseId);
        return ResponseEntity.ok(ApiResponse.ok("Expense details retrieved", response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseResponse>> createExpense(
            @PathVariable Long businessId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ExpenseRequest request) {
        ExpenseResponse created = expenseService.createExpense(businessId, principal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Expense recorded successfully", created));
    }

    @PatchMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> updateExpense(
            @PathVariable Long businessId,
            @PathVariable Long expenseId,
            @Valid @RequestBody ExpenseRequest request) {
        ExpenseResponse updated = expenseService.updateExpense(businessId, expenseId, request);
        return ResponseEntity.ok(ApiResponse.ok("Expense updated successfully", updated));
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<Void>> deleteExpense(
            @PathVariable Long businessId,
            @PathVariable Long expenseId) {
        expenseService.deleteExpense(businessId, expenseId);
        return ResponseEntity.ok(ApiResponse.ok("Expense deleted successfully"));
    }
}
