package com.vyparos.vyparos.business.dto;

import com.vyparos.vyparos.business.entity.Business;
import com.vyparos.vyparos.business.entity.BusinessRole;

import java.time.Instant;

public class BusinessDetailDto {

    private Long id;
    private String name;
    private String businessType;
    private String currency;
    private String timezone;
    private String address;
    private String phone;
    private String email;
    private boolean active;
    private BusinessRole userRole;
    private Instant createdAt;

    public BusinessDetailDto() {}

    public static BusinessDetailDto fromEntity(Business business, BusinessRole userRole) {
        BusinessDetailDto dto = new BusinessDetailDto();
        dto.setId(business.getId());
        dto.setName(business.getName());
        dto.setBusinessType(business.getBusinessType());
        dto.setCurrency(business.getCurrency());
        dto.setTimezone(business.getTimezone());
        dto.setAddress(business.getAddress());
        dto.setPhone(business.getPhone());
        dto.setEmail(business.getEmail());
        dto.setActive(business.isActive());
        dto.setUserRole(userRole);
        dto.setCreatedAt(business.getCreatedAt());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BusinessRole getUserRole() {
        return userRole;
    }

    public void setUserRole(BusinessRole userRole) {
        this.userRole = userRole;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
