package com.vyparos.vyparos.inventory.controller;

import com.vyparos.vyparos.common.dto.ApiResponse;
import com.vyparos.vyparos.common.dto.PageResponse;
import com.vyparos.vyparos.inventory.dto.*;
import com.vyparos.vyparos.inventory.service.InventoryService;
import com.vyparos.vyparos.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/businesses/{businessId}")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getProducts(
            @PathVariable Long businessId,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "false") boolean activeOnly,
            @RequestParam(defaultValue = "false") boolean lowStockOnly,
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        PageResponse<ProductResponse> products = inventoryService.getProducts(businessId, search, activeOnly, lowStockOnly, pageable);
        return ResponseEntity.ok(ApiResponse.ok("Products retrieved successfully", products));
    }

    @GetMapping("/products/low-stock-alerts")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getLowStockAlerts(
            @PathVariable Long businessId) {
        List<ProductResponse> alerts = inventoryService.getLowStockAlerts(businessId);
        return ResponseEntity.ok(ApiResponse.ok("Low stock alerts retrieved", alerts));
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @PathVariable Long businessId,
            @PathVariable Long productId) {
        ProductResponse product = inventoryService.getProductById(businessId, productId);
        return ResponseEntity.ok(ApiResponse.ok("Product details retrieved", product));
    }

    @PostMapping("/products")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @PathVariable Long businessId,
            @Valid @RequestBody ProductRequest request) {
        ProductResponse created = inventoryService.createProduct(businessId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Product added successfully", created));
    }

    @PatchMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long businessId,
            @PathVariable Long productId,
            @Valid @RequestBody ProductRequest request) {
        ProductResponse updated = inventoryService.updateProduct(businessId, productId, request);
        return ResponseEntity.ok(ApiResponse.ok("Product updated successfully", updated));
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<Void>> deactivateProduct(
            @PathVariable Long businessId,
            @PathVariable Long productId) {
        inventoryService.toggleProductStatus(businessId, productId, false);
        return ResponseEntity.ok(ApiResponse.ok("Product deactivated successfully"));
    }

    @PatchMapping("/products/{productId}/status")
    public ResponseEntity<ApiResponse<ProductResponse>> setProductStatus(
            @PathVariable Long businessId,
            @PathVariable Long productId,
            @RequestParam boolean active) {
        inventoryService.toggleProductStatus(businessId, productId, active);
        ProductResponse product = inventoryService.getProductById(businessId, productId);
        return ResponseEntity.ok(ApiResponse.ok("Product status updated successfully", product));
    }

    @PostMapping({"/products/{productId}/stock-movements", "/products/{productId}/stock-movement"})
    public ResponseEntity<ApiResponse<StockMovementResponse>> recordStockMovement(
            @PathVariable Long businessId,
            @PathVariable Long productId,
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody StockMovementRequest request) {
        StockMovementResponse movement = inventoryService.recordStockMovement(
                businessId, productId, principal.getId(), request
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Stock movement recorded successfully", movement));
    }

    @GetMapping("/stock-movements")
    public ResponseEntity<ApiResponse<PageResponse<StockMovementResponse>>> getStockMovements(
            @PathVariable Long businessId,
            @RequestParam(required = false) Long productId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<StockMovementResponse> movements = inventoryService.getStockMovements(businessId, productId, pageable);
        return ResponseEntity.ok(ApiResponse.ok("Stock movements retrieved", movements));
    }
}
