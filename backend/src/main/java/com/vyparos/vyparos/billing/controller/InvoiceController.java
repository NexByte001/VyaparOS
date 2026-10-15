package com.vyparos.vyparos.billing.controller;

import com.vyparos.vyparos.billing.dto.*;
import com.vyparos.vyparos.billing.entity.InvoiceStatus;
import com.vyparos.vyparos.billing.service.BillingService;
import com.vyparos.vyparos.common.dto.ApiResponse;
import com.vyparos.vyparos.common.dto.PageResponse;
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
import java.util.List;

@RestController
@RequestMapping("/api/businesses/{businessId}/invoices")
public class InvoiceController {

    private final BillingService billingService;

    public InvoiceController(BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<InvoiceSummaryResponse>>> getInvoices(
            @PathVariable Long businessId,
            @RequestParam(required = false) InvoiceStatus status,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<InvoiceSummaryResponse> invoices = billingService.getInvoices(
                businessId, status, customerId, startDate, endDate, pageable
        );
        return ResponseEntity.ok(ApiResponse.ok("Invoices retrieved successfully", invoices));
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<ApiResponse<InvoiceDetailResponse>> getInvoiceById(
            @PathVariable Long businessId,
            @PathVariable Long invoiceId) {
        InvoiceDetailResponse invoice = billingService.getInvoiceById(businessId, invoiceId);
        return ResponseEntity.ok(ApiResponse.ok("Invoice details retrieved", invoice));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InvoiceDetailResponse>> createInvoice(
            @PathVariable Long businessId,
            @Valid @RequestBody CreateInvoiceRequest request) {
        InvoiceDetailResponse created = billingService.createInvoice(businessId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Invoice issued successfully", created));
    }

    @PostMapping("/{invoiceId}/payments")
    public ResponseEntity<ApiResponse<InvoiceDetailResponse>> recordPayment(
            @PathVariable Long businessId,
            @PathVariable Long invoiceId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody RecordPaymentRequest request) {
        InvoiceDetailResponse updatedInvoice = billingService.recordPayment(
                businessId, invoiceId, principal.getId(), request
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Payment recorded successfully", updatedInvoice));
    }

    @GetMapping("/{invoiceId}/payments")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getInvoicePayments(
            @PathVariable Long businessId,
            @PathVariable Long invoiceId) {
        List<PaymentResponse> payments = billingService.getInvoicePayments(businessId, invoiceId);
        return ResponseEntity.ok(ApiResponse.ok("Invoice payments retrieved", payments));
    }

    @PostMapping("/{invoiceId}/void")
    public ResponseEntity<ApiResponse<InvoiceDetailResponse>> voidInvoice(
            @PathVariable Long businessId,
            @PathVariable Long invoiceId,
            @Valid @RequestBody VoidInvoiceRequest request) {
        InvoiceDetailResponse voided = billingService.voidInvoice(businessId, invoiceId, request);
        return ResponseEntity.ok(ApiResponse.ok("Invoice voided successfully", voided));
    }
}
