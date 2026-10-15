package com.vyparos.vyparos.services.service;

import com.vyparos.vyparos.services.dto.ServiceRequest;
import com.vyparos.vyparos.services.dto.ServiceResponse;

import java.util.List;

public interface ServiceCatalogService {
    List<ServiceResponse> getServices(Long businessId, boolean activeOnly);
    ServiceResponse getServiceById(Long businessId, Long serviceId);
    ServiceResponse createService(Long businessId, ServiceRequest request);
    ServiceResponse updateService(Long businessId, Long serviceId, ServiceRequest request);
    void toggleServiceStatus(Long businessId, Long serviceId, boolean active);
}
