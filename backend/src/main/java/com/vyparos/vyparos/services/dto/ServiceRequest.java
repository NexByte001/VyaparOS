package com.vyparos.vyparos.services.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ServiceRequest {

    @NotBlank(message = "Service name is required")
    @Size(max = 120)
    private String name;

    private String description;

    @NotNull(message = "Duration in minutes is required")
    @Min(value = 5, message = "Duration must be at least 5 minutes")
    private Integer durationMinutes;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.00", message = "Price cannot be negative")
    private BigDecimal price;

    public ServiceRequest() {}

    public ServiceRequest(String name, String description, Integer durationMinutes, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.price = price;
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
}
