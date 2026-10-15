package com.vyparos.vyparos.business.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateBusinessRequest {

    @NotBlank(message = "Business name is required")
    @Size(max = 150)
    private String name;

    @NotBlank(message = "Business type is required (e.g. Salon, Spa, Gym, Boutique, Repair)")
    @Size(max = 50)
    private String businessType;

    @Pattern(regexp = "^[A-Za-z]{3}$", message = "Currency must be a valid 3-letter ISO-4217 currency code (e.g. INR, USD, EUR)")
    private String currency = "INR";
    private String timezone = "Asia/Kolkata";
    private String address;
    private String phone;
    private String email;

    public CreateBusinessRequest() {}

    public CreateBusinessRequest(String name, String businessType, String currency, String timezone, String address, String phone, String email) {
        this.name = name;
        this.businessType = businessType;
        this.currency = currency != null ? currency : "INR";
        this.timezone = timezone != null ? timezone : "Asia/Kolkata";
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
