package com.vyparos.vyparos.inventory.dto;

import com.vyparos.vyparos.inventory.entity.Product;

import java.math.BigDecimal;
import java.time.Instant;

public class ProductResponse {

    private Long id;
    private Long businessId;
    private String sku;
    private String name;
    private String description;
    private String category;
    private SupplierResponse supplier;
    private BigDecimal purchaseCost;
    private BigDecimal sellingPrice;
    private Integer currentStock;
    private Integer reorderThreshold;
    private boolean lowStock;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    public ProductResponse() {}

    public static ProductResponse fromEntity(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setBusinessId(product.getBusiness().getId());
        response.setSku(product.getSku());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setCategory(product.getCategory());
        if (product.getSupplier() != null) {
            response.setSupplier(SupplierResponse.fromEntity(product.getSupplier()));
        }
        response.setPurchaseCost(product.getPurchaseCost());
        response.setSellingPrice(product.getSellingPrice());
        response.setCurrentStock(product.getCurrentStock());
        response.setReorderThreshold(product.getReorderThreshold());
        response.setLowStock(product.getCurrentStock() <= product.getReorderThreshold());
        response.setActive(product.isActive());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
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

    public SupplierResponse getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierResponse supplier) {
        this.supplier = supplier;
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

    public boolean isLowStock() {
        return lowStock;
    }

    public void setLowStock(boolean lowStock) {
        this.lowStock = lowStock;
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
