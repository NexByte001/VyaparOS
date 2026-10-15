package com.vyparos.vyparos.inventory.dto;

import com.vyparos.vyparos.inventory.entity.MovementType;
import jakarta.validation.constraints.NotNull;

public class StockMovementRequest {

    @NotNull(message = "Movement type is required (RECEIVED, ADJUSTMENT, SALE, RETURN, DAMAGE)")
    private MovementType movementType;

    @NotNull(message = "Quantity change is required (positive to add, negative to remove)")
    private Integer quantityChange;

    private String reason;
    private String referenceId;

    public StockMovementRequest() {}

    public StockMovementRequest(MovementType movementType, Integer quantityChange, String reason, String referenceId) {
        this.movementType = movementType;
        this.quantityChange = quantityChange;
        this.reason = reason;
        this.referenceId = referenceId;
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
}
