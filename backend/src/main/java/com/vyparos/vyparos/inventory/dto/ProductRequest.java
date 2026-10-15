package com.vyparos.vyparos.inventory.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ProductRequest {

    private String sku;

    @NotBlank(message = "Product name is required")
    @Size(max = 150)
    private String name;

    private String description;
    private String category;
    private Long supplierId;

    @NotNull(message = "Purchase cost is required")
    @DecimalMin(value = "0.00", message = "Purchase cost cannot be negative")
    private BigDecimal purchaseCost = BigDecimal.ZERO;

    @NotNull(message = "Selling price is required")
    @DecimalMin(value = "0.00", message = "Selling price cannot be negative")
    private BigDecimal sellingPrice = BigDecimal.ZERO;

    @Min(value = 0, message = "Current stock cannot be negative")
    private Integer currentStock = 0;

    @Min(value = 0, message = "Reorder threshold cannot be negative")
    private Integer reorderThreshold = 5;

    public ProductRequest() {}

    public ProductRequest(String sku, String name, String description, String category, Long supplierId, BigDecimal purchaseCost, BigDecimal sellingPrice, Integer currentStock, Integer reorderThreshold) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.category = category;
        this.supplierId = supplierId;
        this.purchaseCost = purchaseCost;
        this.sellingPrice = sellingPrice;
        this.currentStock = currentStock != null ? currentStock : 0;
        this.reorderThreshold = reorderThreshold != null ? reorderThreshold : 5;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public BigDecimal getPurchaseCost() {
        return purchaseCost;
    }

    public void setPurchaseCost(BigDecimal purchaseCost) {
        this.purchaseCost = purchaseCost;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public Integer getReorderThreshold() {
        return reorderThreshold;
    }

    public void setReorderThreshold(Integer reorderThreshold) {
        this.reorderThreshold = reorderThreshold;
    }
}
