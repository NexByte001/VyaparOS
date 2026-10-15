package com.vyparos.vyparos.billing.dto;

import com.vyparos.vyparos.billing.entity.InvoiceItem;
import com.vyparos.vyparos.billing.entity.InvoiceItemType;

import java.math.BigDecimal;

public class InvoiceItemResponse {

    private Long id;
    private InvoiceItemType itemType;
    private Long itemId;
    private String itemName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;

    public InvoiceItemResponse() {}

    public static InvoiceItemResponse fromEntity(InvoiceItem item) {
        InvoiceItemResponse response = new InvoiceItemResponse();
        response.setId(item.getId());
        response.setItemType(item.getItemType());
        response.setItemId(item.getItemId());
        response.setItemName(item.getItemName());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setLineTotal(item.getLineTotal());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }
}
