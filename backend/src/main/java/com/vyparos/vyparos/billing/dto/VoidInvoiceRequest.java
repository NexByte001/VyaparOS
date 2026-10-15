package com.vyparos.vyparos.billing.dto;

import jakarta.validation.constraints.NotBlank;

public class VoidInvoiceRequest {

    @NotBlank(message = "Reason for voiding the invoice is mandatory")
    private String reason;

    public VoidInvoiceRequest() {}

    public VoidInvoiceRequest(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
