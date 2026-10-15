package com.vyparos.vyparos.billing.service.impl;

import com.vyparos.vyparos.billing.dto.*;
import com.vyparos.vyparos.billing.entity.*;
import com.vyparos.vyparos.billing.repository.InvoiceRepository;
import com.vyparos.vyparos.billing.repository.PaymentRepository;
import com.vyparos.vyparos.billing.service.BillingService;
import com.vyparos.vyparos.business.entity.Business;
import com.vyparos.vyparos.business.entity.BusinessMembership;
import com.vyparos.vyparos.business.entity.BusinessSettings;
import com.vyparos.vyparos.business.repository.BusinessSettingsRepository;
import com.vyparos.vyparos.common.dto.PageResponse;
import com.vyparos.vyparos.customers.entity.Customer;
import com.vyparos.vyparos.customers.repository.CustomerRepository;
import com.vyparos.vyparos.exception.BadRequestException;
import com.vyparos.vyparos.exception.ResourceNotFoundException;
import com.vyparos.vyparos.identity.entity.User;
import com.vyparos.vyparos.identity.repository.UserRepository;
import com.vyparos.vyparos.security.TenantSecurityGuard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class BillingServiceImpl implements BillingService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final BusinessSettingsRepository settingsRepository;
    private final UserRepository userRepository;
    private final TenantSecurityGuard securityGuard;

    public BillingServiceImpl(
            InvoiceRepository invoiceRepository,
            PaymentRepository paymentRepository,
            CustomerRepository customerRepository,
            BusinessSettingsRepository settingsRepository,
            UserRepository userRepository,
            TenantSecurityGuard securityGuard) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.customerRepository = customerRepository;
        this.settingsRepository = settingsRepository;
        this.userRepository = userRepository;
        this.securityGuard = securityGuard;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<InvoiceSummaryResponse> getInvoices(
            Long businessId, InvoiceStatus status, Long customerId,
            LocalDate startDate, LocalDate endDate, Pageable pageable) {
        securityGuard.requireMembership(businessId);
        Page<Invoice> invoicePage = invoiceRepository.findFilteredInvoices(
                businessId, status, customerId, startDate, endDate, pageable
        );
        return PageResponse.from(invoicePage.map(InvoiceSummaryResponse::fromEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceDetailResponse getInvoiceById(Long businessId, Long invoiceId) {
        securityGuard.requireMembership(businessId);
        Invoice invoice = invoiceRepository.findByIdAndBusinessId(invoiceId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found in this business workspace"));
        return InvoiceDetailResponse.fromEntity(invoice);
    }

    @Override
    @Transactional
    public InvoiceDetailResponse createInvoice(Long businessId, CreateInvoiceRequest request) {
        BusinessMembership membership = securityGuard.requireMembership(businessId);
        Business business = membership.getBusiness();

        Customer customer = null;
        if (request.getCustomerId() != null) {
            customer = customerRepository.findByIdAndBusinessId(request.getCustomerId(), businessId)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found in this business workspace"));
        }

        // Generate sequential invoice number
        BusinessSettings settings = settingsRepository.findByBusinessId(businessId)
                .orElseGet(() -> settingsRepository.save(new BusinessSettings(business)));

        long count = invoiceRepository.countByBusinessId(businessId) + 1;
        String prefix = settings.getInvoicePrefix() != null ? settings.getInvoicePrefix() : "INV";
        String invoiceNumber = String.format("%s-%04d", prefix, count);

        Invoice invoice = new Invoice(
                business,
                invoiceNumber,
                customer,
                request.getInvoiceDate(),
                request.getDueDate(),
                request.getNotes()
        );

        // Calculate line item totals
        BigDecimal subtotal = BigDecimal.ZERO;
        for (InvoiceItemRequest itemReq : request.getItems()) {
            BigDecimal lineTotal = itemReq.getUnitPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            InvoiceItem item = new InvoiceItem(
                    itemReq.getItemType(),
                    itemReq.getItemId(),
                    itemReq.getItemName().trim(),
                    itemReq.getQuantity(),
                    itemReq.getUnitPrice(),
                    lineTotal
            );
            invoice.addItem(item);
            subtotal = subtotal.add(lineTotal);
        }

        invoice.setSubtotal(subtotal);

        // Apply discount
        BigDecimal discount = request.getDiscountAmount() != null ? request.getDiscountAmount() : BigDecimal.ZERO;
        if (discount.compareTo(subtotal) > 0) {
            throw new BadRequestException("Discount cannot exceed the invoice subtotal");
        }
        invoice.setDiscountAmount(discount);

        // Apply tax from business settings
        BigDecimal taxRate = settings.getTaxRate() != null ? settings.getTaxRate() : BigDecimal.ZERO;
        BigDecimal taxableAmount = subtotal.subtract(discount);
        BigDecimal taxAmount = taxableAmount.multiply(taxRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        invoice.setTaxAmount(taxAmount);

        // Calculate final total & balance
        BigDecimal totalAmount = taxableAmount.add(taxAmount);
        invoice.setTotalAmount(totalAmount);
        invoice.setPaidAmount(BigDecimal.ZERO);
        invoice.setBalanceDue(totalAmount);
        invoice.setStatus(totalAmount.compareTo(BigDecimal.ZERO) == 0 ? InvoiceStatus.PAID : InvoiceStatus.ISSUED);

        Invoice saved = invoiceRepository.save(invoice);
        return InvoiceDetailResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public InvoiceDetailResponse recordPayment(Long businessId, Long invoiceId, Long recordedByUserId, RecordPaymentRequest request) {
        securityGuard.requireMembership(businessId);

        Invoice invoice = invoiceRepository.findByIdAndBusinessId(invoiceId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found in this business workspace"));

        if (invoice.getStatus() == InvoiceStatus.VOID) {
            throw new BadRequestException("Cannot record payment against a voided invoice");
        }

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new BadRequestException("Invoice is already fully paid");
        }

        BigDecimal paymentAmount = request.getAmount();
        if (paymentAmount.compareTo(invoice.getBalanceDue()) > 0) {
            throw new BadRequestException("Payment amount (" + paymentAmount + ") exceeds remaining balance due (" + invoice.getBalanceDue() + ")");
        }

        User recordedByUser = userRepository.findById(recordedByUserId).orElse(null);

        Payment payment = new Payment(
                invoice.getBusiness(),
                invoice,
                paymentAmount,
                request.getPaymentMethod(),
                request.getTransactionReference(),
                recordedByUser,
                request.getNotes()
        );

        invoice.addPayment(payment);
        paymentRepository.save(payment);

        BigDecimal newPaidAmount = invoice.getPaidAmount().add(paymentAmount);
        BigDecimal newBalanceDue = invoice.getTotalAmount().subtract(newPaidAmount);

        invoice.setPaidAmount(newPaidAmount);
        invoice.setBalanceDue(newBalanceDue);

        if (newBalanceDue.compareTo(BigDecimal.ZERO) <= 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        } else {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        }

        Invoice saved = invoiceRepository.save(invoice);
        return InvoiceDetailResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getInvoicePayments(Long businessId, Long invoiceId) {
        securityGuard.requireMembership(businessId);
        Invoice invoice = invoiceRepository.findByIdAndBusinessId(invoiceId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found in this business workspace"));
        return paymentRepository.findAllByInvoiceIdOrderByPaymentDateAsc(invoice.getId()).stream()
                .map(PaymentResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public InvoiceDetailResponse voidInvoice(Long businessId, Long invoiceId, VoidInvoiceRequest request) {
        securityGuard.requireAdminOrOwner(businessId);

        Invoice invoice = invoiceRepository.findByIdAndBusinessId(invoiceId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found in this business workspace"));

        if (invoice.getStatus() == InvoiceStatus.VOID) {
            throw new BadRequestException("Invoice is already void");
        }

        if (invoice.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) {
            throw new BadRequestException("Cannot void an invoice with recorded payments. Please process a reversal or refund first.");
        }

        invoice.setStatus(InvoiceStatus.VOID);
        invoice.setNotes((invoice.getNotes() != null ? invoice.getNotes() + "\n" : "") + "[VOIDED]: " + request.getReason().trim());

        Invoice saved = invoiceRepository.save(invoice);
        return InvoiceDetailResponse.fromEntity(saved);
    }
}
