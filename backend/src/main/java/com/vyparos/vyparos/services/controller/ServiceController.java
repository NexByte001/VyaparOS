package com.vyparos.vyparos.services.controller;

import com.vyparos.vyparos.common.dto.ApiResponse;
import com.vyparos.vyparos.services.dto.ServiceRequest;
import com.vyparos.vyparos.services.dto.ServiceResponse;
import com.vyparos.vyparos.services.service.ServiceCatalogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/businesses/{businessId}/services")
public class ServiceController {

    private final ServiceCatalogService serviceCatalogService;

    public ServiceController(ServiceCatalogService serviceCatalogService) {
        this.serviceCatalogService = serviceCatalogService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ServiceResponse>>> getServices(
            @PathVariable Long businessId,
            @RequestParam(defaultValue = "false") boolean activeOnly) {
        List<ServiceResponse> services = serviceCatalogService.getServices(businessId, activeOnly);
        return ResponseEntity.ok(ApiResponse.ok("Services retrieved successfully", services));
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<ApiResponse<ServiceResponse>> getServiceById(
            @PathVariable Long businessId,
            @PathVariable Long serviceId) {
        ServiceResponse service = serviceCatalogService.getServiceById(businessId, serviceId);
        return ResponseEntity.ok(ApiResponse.ok("Service details retrieved", service));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ServiceResponse>> createService(
            @PathVariable Long businessId,
            @Valid @RequestBody ServiceRequest request) {
        ServiceResponse service = serviceCatalogService.createService(businessId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Service created successfully", service));
    }

    @PatchMapping("/{serviceId}")
    public ResponseEntity<ApiResponse<ServiceResponse>> updateService(
            @PathVariable Long businessId,
            @PathVariable Long serviceId,
            @Valid @RequestBody ServiceRequest request) {
        ServiceResponse service = serviceCatalogService.updateService(businessId, serviceId, request);
        return ResponseEntity.ok(ApiResponse.ok("Service updated successfully", service));
    }

    @DeleteMapping("/{serviceId}")
    public ResponseEntity<ApiResponse<Void>> deactivateService(
            @PathVariable Long businessId,
            @PathVariable Long serviceId) {
        serviceCatalogService.toggleServiceStatus(businessId, serviceId, false);
        return ResponseEntity.ok(ApiResponse.ok("Service deactivated successfully"));
    }
}
