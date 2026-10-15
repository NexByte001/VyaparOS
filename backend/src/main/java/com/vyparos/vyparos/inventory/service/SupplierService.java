package com.vyparos.vyparos.inventory.service;

import com.vyparos.vyparos.inventory.dto.SupplierRequest;
import com.vyparos.vyparos.inventory.dto.SupplierResponse;

import java.util.List;

public interface SupplierService {
    List<SupplierResponse> getSuppliers(Long businessId, boolean activeOnly);
    SupplierResponse getSupplierById(Long businessId, Long supplierId);
    SupplierResponse createSupplier(Long businessId, SupplierRequest request);
    SupplierResponse updateSupplier(Long businessId, Long supplierId, SupplierRequest request);
    void toggleSupplierStatus(Long businessId, Long supplierId, boolean active);
}
