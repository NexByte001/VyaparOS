package com.vyparos.vyparos.billing.dto;

import com.vyparos.vyparos.billing.entity.InvoiceItemType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class InvoiceItemRequest {

    @NotNull(message = "Item type is required (SERVICE, PRODUCT, CUSTOM)")
    private InvoiceItemType itemType;

    private Long itemId;

    @NotBlank(message = "Item name is required")
    private String itemName;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity = 1;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.00", message = "Unit price cannot be negative")
    private BigDecimal unitPrice;

    public InvoiceItemRequest() {}

    public InvoiceItemRequest(InvoiceItemType itemType, Long itemId, String itemName, Integer quantity, BigDecimal unitPrice) {
        this.itemType = itemType;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public InvoiceItemType getItemType() {
        return itemType;
    }

    public void setItemType(InvoiceItemType itemType) {
        this.itemType = itemType;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
