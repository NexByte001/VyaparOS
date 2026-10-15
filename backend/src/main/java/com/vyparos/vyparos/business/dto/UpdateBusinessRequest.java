package com.vyparos.vyparos.business.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateBusinessRequest {

    @Size(max = 150)
    private String name;

    @Size(max = 50)
    private String businessType;

    @Pattern(regexp = "^[A-Za-z]{3}$", message = "Currency must be a valid 3-letter ISO-4217 currency code (e.g. INR, USD, EUR)")
    private String currency;
    private String timezone;
    private String address;
    private String phone;
    private String email;

    public UpdateBusinessRequest() {}

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
