package com.vyparos.vyparos.business.dto;

import com.vyparos.vyparos.business.entity.BusinessSettings;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;
import java.time.LocalTime;

public class BusinessSettingsDto {

    @DecimalMin(value = "0.00", message = "Tax rate cannot be negative")
    @DecimalMax(value = "100.00", message = "Tax rate cannot exceed 100%")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal taxRate;

    private String taxName;
    private String invoicePrefix;
    private Integer defaultAppointmentDuration;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private String settingsJson;

    public BusinessSettingsDto() {}

    public static BusinessSettingsDto fromEntity(BusinessSettings settings) {
        BusinessSettingsDto dto = new BusinessSettingsDto();
        dto.setTaxRate(settings.getTaxRate());
        dto.setTaxName(settings.getTaxName());
        dto.setInvoicePrefix(settings.getInvoicePrefix());
        dto.setDefaultAppointmentDuration(settings.getDefaultAppointmentDuration());
        dto.setOpeningTime(settings.getOpeningTime());
        dto.setClosingTime(settings.getClosingTime());
        dto.setSettingsJson(settings.getSettingsJson());
        return dto;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getInvoicePrefix() {
        return invoicePrefix;
    }

    public void setInvoicePrefix(String invoicePrefix) {
        this.invoicePrefix = invoicePrefix;
    }

    public Integer getDefaultAppointmentDuration() {
        return defaultAppointmentDuration;
    }

    public void setDefaultAppointmentDuration(Integer defaultAppointmentDuration) {
        this.defaultAppointmentDuration = defaultAppointmentDuration;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public String getSettingsJson() {
        return settingsJson;
    }

    public void setSettingsJson(String settingsJson) {
        this.settingsJson = settingsJson;
    }
}
