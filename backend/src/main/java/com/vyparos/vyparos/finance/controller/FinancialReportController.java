package com.vyparos.vyparos.finance.controller;

import com.vyparos.vyparos.common.dto.ApiResponse;
import com.vyparos.vyparos.finance.dto.FinancialReportResponse;
import com.vyparos.vyparos.finance.service.FinancialReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/businesses/{businessId}/reports")
public class FinancialReportController {

    private final FinancialReportService financialReportService;

    public FinancialReportController(FinancialReportService financialReportService) {
        this.financialReportService = financialReportService;
    }

    @GetMapping("/financial-summary")
    public ResponseEntity<ApiResponse<FinancialReportResponse>> getFinancialSummary(
            @PathVariable Long businessId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        FinancialReportResponse report = financialReportService.getFinancialSummary(businessId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.ok("Financial report summary retrieved", report));
    }
}
