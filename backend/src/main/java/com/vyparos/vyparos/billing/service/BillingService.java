package com.vyparos.vyparos.billing.service;

import com.vyparos.vyparos.billing.dto.*;
import com.vyparos.vyparos.billing.entity.InvoiceStatus;
import com.vyparos.vyparos.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface BillingService {
    PageResponse<InvoiceSummaryResponse> getInvoices(Long businessId, InvoiceStatus status, Long customerId, LocalDate startDate, LocalDate endDate, Pageable pageable);
    InvoiceDetailResponse getInvoiceById(Long businessId, Long invoiceId);
    InvoiceDetailResponse createInvoice(Long businessId, CreateInvoiceRequest request);
    InvoiceDetailResponse recordPayment(Long businessId, Long invoiceId, Long recordedByUserId, RecordPaymentRequest request);
    List<PaymentResponse> getInvoicePayments(Long businessId, Long invoiceId);
    InvoiceDetailResponse voidInvoice(Long businessId, Long invoiceId, VoidInvoiceRequest request);
}
