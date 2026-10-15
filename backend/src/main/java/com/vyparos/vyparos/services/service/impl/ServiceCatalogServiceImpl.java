package com.vyparos.vyparos.services.service.impl;

import com.vyparos.vyparos.business.entity.BusinessMembership;
import com.vyparos.vyparos.exception.ResourceNotFoundException;
import com.vyparos.vyparos.security.TenantSecurityGuard;
import com.vyparos.vyparos.services.dto.ServiceRequest;
import com.vyparos.vyparos.services.dto.ServiceResponse;
import com.vyparos.vyparos.services.entity.ServiceEntity;
import com.vyparos.vyparos.services.repository.ServiceRepository;
import com.vyparos.vyparos.services.service.ServiceCatalogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServiceCatalogServiceImpl implements ServiceCatalogService {

    private final ServiceRepository serviceRepository;
    private final TenantSecurityGuard securityGuard;

    public ServiceCatalogServiceImpl(ServiceRepository serviceRepository, TenantSecurityGuard securityGuard) {
        this.serviceRepository = serviceRepository;
        this.securityGuard = securityGuard;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceResponse> getServices(Long businessId, boolean activeOnly) {
        securityGuard.requireMembership(businessId);
        List<ServiceEntity> services = activeOnly
                ? serviceRepository.findAllByBusinessIdAndActiveTrue(businessId)
                : serviceRepository.findAllByBusinessId(businessId);

        return services.stream().map(ServiceResponse::fromEntity).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResponse getServiceById(Long businessId, Long serviceId) {
        securityGuard.requireMembership(businessId);
        ServiceEntity service = serviceRepository.findByIdAndBusinessId(serviceId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found in this business workspace"));
        return ServiceResponse.fromEntity(service);
    }

    @Override
    @Transactional
    public ServiceResponse createService(Long businessId, ServiceRequest request) {
        BusinessMembership adminMembership = securityGuard.requireAdminOrOwner(businessId);

        ServiceEntity service = new ServiceEntity(
                adminMembership.getBusiness(),
                request.getName().trim(),
                request.getDescription() != null ? request.getDescription().trim() : null,
                request.getDurationMinutes(),
                request.getPrice()
        );

        ServiceEntity saved = serviceRepository.save(service);
        return ServiceResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public ServiceResponse updateService(Long businessId, Long serviceId, ServiceRequest request) {
        securityGuard.requireAdminOrOwner(businessId);

        ServiceEntity service = serviceRepository.findByIdAndBusinessId(serviceId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found in this business workspace"));

        if (request.getName() != null && !request.getName().isBlank()) {
            service.setName(request.getName().trim());
        }
        if (request.getDescription() != null) {
            service.setDescription(request.getDescription().trim());
        }
        if (request.getDurationMinutes() != null) {
            service.setDurationMinutes(request.getDurationMinutes());
        }
        if (request.getPrice() != null) {
            service.setPrice(request.getPrice());
        }

        ServiceEntity saved = serviceRepository.save(service);
        return ServiceResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public void toggleServiceStatus(Long businessId, Long serviceId, boolean active) {
        securityGuard.requireAdminOrOwner(businessId);

        ServiceEntity service = serviceRepository.findByIdAndBusinessId(serviceId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found in this business workspace"));

        service.setActive(active);
        serviceRepository.save(service);
    }
}
