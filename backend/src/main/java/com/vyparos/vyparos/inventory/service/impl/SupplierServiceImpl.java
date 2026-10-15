package com.vyparos.vyparos.inventory.service.impl;

import com.vyparos.vyparos.business.entity.BusinessMembership;
import com.vyparos.vyparos.exception.ResourceNotFoundException;
import com.vyparos.vyparos.inventory.dto.SupplierRequest;
import com.vyparos.vyparos.inventory.dto.SupplierResponse;
import com.vyparos.vyparos.inventory.entity.Supplier;
import com.vyparos.vyparos.inventory.repository.SupplierRepository;
import com.vyparos.vyparos.inventory.service.SupplierService;
import com.vyparos.vyparos.security.TenantSecurityGuard;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final TenantSecurityGuard securityGuard;

    public SupplierServiceImpl(SupplierRepository supplierRepository, TenantSecurityGuard securityGuard) {
        this.supplierRepository = supplierRepository;
        this.securityGuard = securityGuard;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponse> getSuppliers(Long businessId, boolean activeOnly) {
        securityGuard.requireMembership(businessId);
        List<Supplier> suppliers = activeOnly
                ? supplierRepository.findAllByBusinessIdAndActiveTrue(businessId)
                : supplierRepository.findAllByBusinessId(businessId);
        return suppliers.stream().map(SupplierResponse::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getSupplierById(Long businessId, Long supplierId) {
        securityGuard.requireMembership(businessId);
        Supplier supplier = supplierRepository.findByIdAndBusinessId(supplierId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found in this business workspace"));
        return SupplierResponse.fromEntity(supplier);
    }

    @Override
    @Transactional
    public SupplierResponse createSupplier(Long businessId, SupplierRequest request) {
        BusinessMembership adminMembership = securityGuard.requireAdminOrOwner(businessId);

        Supplier supplier = new Supplier(
                adminMembership.getBusiness(),
                request.getName().trim(),
                request.getContactPerson() != null ? request.getContactPerson().trim() : null,
                request.getPhone() != null ? request.getPhone().trim() : null,
                request.getEmail() != null ? request.getEmail().trim() : null,
                request.getAddress() != null ? request.getAddress().trim() : null
        );

        Supplier saved = supplierRepository.save(supplier);
        return SupplierResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public SupplierResponse updateSupplier(Long businessId, Long supplierId, SupplierRequest request) {
        securityGuard.requireAdminOrOwner(businessId);

        Supplier supplier = supplierRepository.findByIdAndBusinessId(supplierId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found in this business workspace"));

        if (request.getName() != null && !request.getName().isBlank()) {
            supplier.setName(request.getName().trim());
        }
        if (request.getContactPerson() != null) {
            supplier.setContactPerson(request.getContactPerson().trim());
        }
        if (request.getPhone() != null) {
            supplier.setPhone(request.getPhone().trim());
        }
        if (request.getEmail() != null) {
            supplier.setEmail(request.getEmail().trim());
        }
        if (request.getAddress() != null) {
            supplier.setAddress(request.getAddress().trim());
        }

        Supplier saved = supplierRepository.save(supplier);
        return SupplierResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public void toggleSupplierStatus(Long businessId, Long supplierId, boolean active) {
        securityGuard.requireAdminOrOwner(businessId);

        Supplier supplier = supplierRepository.findByIdAndBusinessId(supplierId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found in this business workspace"));

        supplier.setActive(active);
        supplierRepository.save(supplier);
    }
}
