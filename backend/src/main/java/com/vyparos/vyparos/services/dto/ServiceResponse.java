package com.vyparos.vyparos.services.dto;

import com.vyparos.vyparos.services.entity.ServiceEntity;
import java.math.BigDecimal;
import java.time.Instant;

public class ServiceResponse {

    private Long id;
    private Long businessId;
    private String name;
    private String description;
    private Integer durationMinutes;
    private BigDecimal price;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    public ServiceResponse() {}

    public static ServiceResponse fromEntity(ServiceEntity service) {
        ServiceResponse response = new ServiceResponse();
        response.setId(service.getId());
        response.setBusinessId(service.getBusiness().getId());
        response.setName(service.getName());
        response.setDescription(service.getDescription());
        response.setDurationMinutes(service.getDurationMinutes());
        response.setPrice(service.getPrice());
        response.setActive(service.isActive());
        response.setCreatedAt(service.getCreatedAt());
        response.setUpdatedAt(service.getUpdatedAt());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
