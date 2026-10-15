package com.vyparos.vyparos.inventory.controller;

import com.vyparos.vyparos.common.dto.ApiResponse;
import com.vyparos.vyparos.inventory.dto.SupplierRequest;
import com.vyparos.vyparos.inventory.dto.SupplierResponse;
import com.vyparos.vyparos.inventory.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/businesses/{businessId}/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SupplierResponse>>> getSuppliers(
            @PathVariable Long businessId,
            @RequestParam(defaultValue = "false") boolean activeOnly) {
        List<SupplierResponse> suppliers = supplierService.getSuppliers(businessId, activeOnly);
        return ResponseEntity.ok(ApiResponse.ok("Suppliers retrieved successfully", suppliers));
    }

    @GetMapping("/{supplierId}")
    public ResponseEntity<ApiResponse<SupplierResponse>> getSupplierById(
            @PathVariable Long businessId,
            @PathVariable Long supplierId) {
        SupplierResponse supplier = supplierService.getSupplierById(businessId, supplierId);
        return ResponseEntity.ok(ApiResponse.ok("Supplier details retrieved", supplier));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SupplierResponse>> createSupplier(
            @PathVariable Long businessId,
            @Valid @RequestBody SupplierRequest request) {
        SupplierResponse created = supplierService.createSupplier(businessId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Supplier added successfully", created));
    }

    @PatchMapping("/{supplierId}")
    public ResponseEntity<ApiResponse<SupplierResponse>> updateSupplier(
            @PathVariable Long businessId,
            @PathVariable Long supplierId,
            @Valid @RequestBody SupplierRequest request) {
        SupplierResponse updated = supplierService.updateSupplier(businessId, supplierId, request);
        return ResponseEntity.ok(ApiResponse.ok("Supplier updated successfully", updated));
    }

    @DeleteMapping("/{supplierId}")
    public ResponseEntity<ApiResponse<Void>> deactivateSupplier(
            @PathVariable Long businessId,
            @PathVariable Long supplierId) {
        supplierService.toggleSupplierStatus(businessId, supplierId, false);
        return ResponseEntity.ok(ApiResponse.ok("Supplier deactivated successfully"));
    }
}
