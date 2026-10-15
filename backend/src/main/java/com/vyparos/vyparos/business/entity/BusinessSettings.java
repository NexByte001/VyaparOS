package com.vyparos.vyparos.business.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;

@Entity
@Table(name = "business_settings")
public class BusinessSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id", nullable = false, unique = true)
    private Business business;

    @Column(name = "tax_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxRate = BigDecimal.ZERO;

    @Column(name = "tax_name", length = 50)
    private String taxName = "GST";

    @Column(name = "invoice_prefix", length = 20)
    private String invoicePrefix = "INV";

    @Column(name = "default_appointment_duration")
    private Integer defaultAppointmentDuration = 30;

    @Column(name = "opening_time")
    private LocalTime openingTime = LocalTime.of(9, 0);

    @Column(name = "closing_time")
    private LocalTime closingTime = LocalTime.of(20, 0);

    @Column(name = "settings_json", columnDefinition = "TEXT")
    private String settingsJson;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    public BusinessSettings() {}

    public BusinessSettings(Business business) {
        this.business = business;
        this.taxRate = BigDecimal.ZERO;
        this.taxName = "GST";
        this.invoicePrefix = "INV";
        this.defaultAppointmentDuration = 30;
        this.openingTime = LocalTime.of(9, 0);
        this.closingTime = LocalTime.of(20, 0);
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
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

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
