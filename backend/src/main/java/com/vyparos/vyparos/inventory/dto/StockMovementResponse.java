package com.vyparos.vyparos.inventory.dto;

import com.vyparos.vyparos.identity.dto.UserSummaryDto;
import com.vyparos.vyparos.inventory.entity.MovementType;
import com.vyparos.vyparos.inventory.entity.StockMovement;

import java.time.Instant;

public class StockMovementResponse {

    private Long id;
    private Long businessId;
    private Long productId;
    private String productName;
    private String productSku;
    private MovementType movementType;
    private Integer quantityChange;
    private Integer previousStock;
    private Integer newStock;
    private String reason;
    private String referenceId;
    private UserSummaryDto recordedBy;
    private Instant createdAt;

    public StockMovementResponse() {}

    public static StockMovementResponse fromEntity(StockMovement movement) {
        StockMovementResponse response = new StockMovementResponse();
        response.setId(movement.getId());
        response.setBusinessId(movement.getBusiness().getId());
        response.setProductId(movement.getProduct().getId());
        response.setProductName(movement.getProduct().getName());
        response.setProductSku(movement.getProduct().getSku());
        response.setMovementType(movement.getMovementType());
        response.setQuantityChange(movement.getQuantityChange());
        response.setPreviousStock(movement.getPreviousStock());
        response.setNewStock(movement.getNewStock());
        response.setReason(movement.getReason());
        response.setReferenceId(movement.getReferenceId());
        if (movement.getRecordedByUser() != null) {
            response.setRecordedBy(UserSummaryDto.fromEntity(movement.getRecordedByUser()));
        }
        response.setCreatedAt(movement.getCreatedAt());
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public Integer getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(Integer quantityChange) {
        this.quantityChange = quantityChange;
    }

    public Integer getPreviousStock() {
        return previousStock;
    }

    public void setPreviousStock(Integer previousStock) {
        this.previousStock = previousStock;
    }

    public Integer getNewStock() {
        return newStock;
    }

    public void setNewStock(Integer newStock) {
        this.newStock = newStock;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public UserSummaryDto getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(UserSummaryDto recordedBy) {
        this.recordedBy = recordedBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
