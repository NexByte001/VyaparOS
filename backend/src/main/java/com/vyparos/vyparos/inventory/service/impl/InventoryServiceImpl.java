package com.vyparos.vyparos.inventory.service.impl;

import com.vyparos.vyparos.business.entity.BusinessMembership;
import com.vyparos.vyparos.common.dto.PageResponse;
import com.vyparos.vyparos.exception.BadRequestException;
import com.vyparos.vyparos.exception.ConflictException;
import com.vyparos.vyparos.exception.ResourceNotFoundException;
import com.vyparos.vyparos.identity.entity.User;
import com.vyparos.vyparos.identity.repository.UserRepository;
import com.vyparos.vyparos.inventory.dto.*;
import com.vyparos.vyparos.inventory.entity.MovementType;
import com.vyparos.vyparos.inventory.entity.Product;
import com.vyparos.vyparos.inventory.entity.StockMovement;
import com.vyparos.vyparos.inventory.entity.Supplier;
import com.vyparos.vyparos.inventory.repository.ProductRepository;
import com.vyparos.vyparos.inventory.repository.StockMovementRepository;
import com.vyparos.vyparos.inventory.repository.SupplierRepository;
import com.vyparos.vyparos.inventory.service.InventoryService;
import com.vyparos.vyparos.security.TenantSecurityGuard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final StockMovementRepository stockMovementRepository;
    private final UserRepository userRepository;
    private final TenantSecurityGuard securityGuard;

    public InventoryServiceImpl(
            ProductRepository productRepository,
            SupplierRepository supplierRepository,
            StockMovementRepository stockMovementRepository,
            UserRepository userRepository,
            TenantSecurityGuard securityGuard) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.stockMovementRepository = stockMovementRepository;
        this.userRepository = userRepository;
        this.securityGuard = securityGuard;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getProducts(
            Long businessId, String query, boolean activeOnly, boolean lowStockOnly, Pageable pageable) {
        securityGuard.requireMembership(businessId);
        Page<Product> productPage = productRepository.searchProducts(businessId, query, activeOnly, lowStockOnly, pageable);
        return PageResponse.from(productPage.map(ProductResponse::fromEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long businessId, Long productId) {
        securityGuard.requireMembership(businessId);
        Product product = productRepository.findByIdAndBusinessId(productId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in this business workspace"));
        return ProductResponse.fromEntity(product);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(Long businessId, ProductRequest request) {
        BusinessMembership adminMembership = securityGuard.requireAdminOrOwner(businessId);

        String trimmedName = request.getName() != null ? request.getName().trim() : "";
        if (trimmedName.isEmpty()) {
            throw new BadRequestException("Product name cannot be empty");
        }
        if (productRepository.findByBusinessIdAndNameIgnoreCase(businessId, trimmedName).isPresent()) {
            throw new ConflictException("A product named '" + trimmedName + "' already exists in this business workspace");
        }

        String trimmedSku = (request.getSku() != null && !request.getSku().trim().isEmpty())
                ? request.getSku().trim()
                : null;
        if (trimmedSku != null && productRepository.findByBusinessIdAndSkuIgnoreCase(businessId, trimmedSku).isPresent()) {
            throw new ConflictException("A product with SKU '" + trimmedSku + "' already exists in this business workspace");
        }

        Supplier supplier = null;
        if (request.getSupplierId() != null) {
            supplier = supplierRepository.findByIdAndBusinessId(request.getSupplierId(), businessId)
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found in this business workspace"));
        }

        Product product = new Product(
                adminMembership.getBusiness(),
                trimmedSku,
                trimmedName,
                request.getDescription() != null ? request.getDescription().trim() : null,
                request.getCategory() != null ? request.getCategory().trim() : null,
                supplier,
                request.getPurchaseCost(),
                request.getSellingPrice(),
                request.getCurrentStock(),
                request.getReorderThreshold()
        );

        Product saved = productRepository.save(product);

        if (saved.getCurrentStock() != null && saved.getCurrentStock() > 0) {
            StockMovement initialMovement = new StockMovement(
                    saved.getBusiness(),
                    saved,
                    MovementType.RECEIVED,
                    saved.getCurrentStock(),
                    0,
                    saved.getCurrentStock(),
                    "Initial stock upon product creation",
                    null,
                    adminMembership.getUser()
            );
            stockMovementRepository.save(initialMovement);
        }

        return ProductResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long businessId, Long productId, ProductRequest request) {
        BusinessMembership adminMembership = securityGuard.requireAdminOrOwner(businessId);

        Product product = productRepository.findByIdAndBusinessId(productId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in this business workspace"));

        if (request.getName() != null && !request.getName().isBlank()) {
            String trimmedName = request.getName().trim();
            if (productRepository.existsByBusinessIdAndNameIgnoreCaseAndIdNot(businessId, trimmedName, productId)) {
                throw new ConflictException("A product named '" + trimmedName + "' already exists in this business workspace");
            }
            product.setName(trimmedName);
        }

        if (request.getSku() != null) {
            String trimmedSku = request.getSku().trim();
            if (trimmedSku.isEmpty()) {
                product.setSku(null);
            } else {
                if (productRepository.existsByBusinessIdAndSkuIgnoreCaseAndIdNot(businessId, trimmedSku, productId)) {
                    throw new ConflictException("A product with SKU '" + trimmedSku + "' already exists in this business workspace");
                }
                product.setSku(trimmedSku);
            }
        }

        if (request.getDescription() != null) product.setDescription(request.getDescription().trim());
        if (request.getCategory() != null) product.setCategory(request.getCategory().trim());

        if (request.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findByIdAndBusinessId(request.getSupplierId(), businessId)
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found in this business workspace"));
            product.setSupplier(supplier);
        }

        if (request.getPurchaseCost() != null) product.setPurchaseCost(request.getPurchaseCost());
        if (request.getSellingPrice() != null) product.setSellingPrice(request.getSellingPrice());
        if (request.getReorderThreshold() != null) product.setReorderThreshold(request.getReorderThreshold());

        // Support updating stock quantity directly
        if (request.getCurrentStock() != null && !request.getCurrentStock().equals(product.getCurrentStock())) {
            int oldStock = product.getCurrentStock() != null ? product.getCurrentStock() : 0;
            int newStock = request.getCurrentStock();
            if (newStock < 0) {
                throw new BadRequestException("Current stock cannot be negative");
            }
            int diff = newStock - oldStock;
            product.setCurrentStock(newStock);

            StockMovement directMovement = new StockMovement(
                    product.getBusiness(),
                    product,
                    diff >= 0 ? MovementType.RECEIVED : MovementType.ADJUSTMENT,
                    diff,
                    oldStock,
                    newStock,
                    "Direct stock adjustment via product edit",
                    null,
                    adminMembership.getUser()
            );
            stockMovementRepository.save(directMovement);
        }

        Product saved = productRepository.save(product);
        return ProductResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public void toggleProductStatus(Long businessId, Long productId, boolean active) {
        securityGuard.requireAdminOrOwner(businessId);

        Product product = productRepository.findByIdAndBusinessId(productId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in this business workspace"));

        product.setActive(active);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public StockMovementResponse recordStockMovement(Long businessId, Long productId, Long userId, StockMovementRequest request) {
        securityGuard.requireAdminOrOwner(businessId);

        Product product = productRepository.findByIdAndBusinessId(productId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in this business workspace"));

        int previousStock = product.getCurrentStock();
        int newStock = previousStock + request.getQuantityChange();

        if (newStock < 0) {
            throw new BadRequestException("Stock reduction (" + Math.abs(request.getQuantityChange()) +
                    ") exceeds available stock (" + previousStock + "). Negative stock is prevented.");
        }

        product.setCurrentStock(newStock);
        productRepository.save(product);

        User user = userRepository.findById(userId).orElse(null);

        StockMovement movement = new StockMovement(
                product.getBusiness(),
                product,
                request.getMovementType(),
                request.getQuantityChange(),
                previousStock,
                newStock,
                request.getReason(),
                request.getReferenceId(),
                user
        );

        StockMovement saved = stockMovementRepository.save(movement);
        return StockMovementResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<StockMovementResponse> getStockMovements(Long businessId, Long productId, Pageable pageable) {
        securityGuard.requireAdminOrOwner(businessId);
        Page<StockMovement> page = stockMovementRepository.findFilteredStockMovements(businessId, productId, pageable);
        return PageResponse.from(page.map(StockMovementResponse::fromEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getLowStockAlerts(Long businessId) {
        securityGuard.requireMembership(businessId);
        List<Product> lowStock = productRepository.findLowStockProducts(businessId);
        return lowStock.stream().map(ProductResponse::fromEntity).toList();
    }
}
