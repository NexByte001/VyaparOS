package com.vyparos.vyparos.finance.service;

import com.vyparos.vyparos.finance.dto.FinancialReportResponse;

import java.time.LocalDate;

public interface FinancialReportService {
    FinancialReportResponse getFinancialSummary(Long businessId, LocalDate startDate, LocalDate endDate);
}
