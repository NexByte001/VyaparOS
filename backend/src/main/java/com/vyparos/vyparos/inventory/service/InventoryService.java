package com.vyparos.vyparos.inventory.service;

import com.vyparos.vyparos.common.dto.PageResponse;
import com.vyparos.vyparos.inventory.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryService {
    PageResponse<ProductResponse> getProducts(Long businessId, String query, boolean activeOnly, boolean lowStockOnly, Pageable pageable);
    ProductResponse getProductById(Long businessId, Long productId);
    ProductResponse createProduct(Long businessId, ProductRequest request);
    ProductResponse updateProduct(Long businessId, Long productId, ProductRequest request);
    void toggleProductStatus(Long businessId, Long productId, boolean active);
    StockMovementResponse recordStockMovement(Long businessId, Long productId, Long userId, StockMovementRequest request);
    PageResponse<StockMovementResponse> getStockMovements(Long businessId, Long productId, Pageable pageable);
    List<ProductResponse> getLowStockAlerts(Long businessId);
}
