package com.vyparos.vyparos.customers.controller;

import com.vyparos.vyparos.common.dto.ApiResponse;
import com.vyparos.vyparos.common.dto.PageResponse;
import com.vyparos.vyparos.customers.dto.CustomerRequest;
import com.vyparos.vyparos.customers.dto.CustomerResponse;
import com.vyparos.vyparos.customers.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/businesses/{businessId}/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CustomerResponse>>> getCustomers(
            @PathVariable Long businessId,
            @RequestParam(required = false) String search,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponse<CustomerResponse> response = customerService.getCustomers(businessId, search, pageable);
        return ResponseEntity.ok(ApiResponse.ok("Customers retrieved successfully", response));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(
            @PathVariable Long businessId,
            @PathVariable Long customerId) {
        CustomerResponse response = customerService.getCustomerById(businessId, customerId);
        return ResponseEntity.ok(ApiResponse.ok("Customer details retrieved", response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(
            @PathVariable Long businessId,
            @Valid @RequestBody CustomerRequest request) {
        CustomerResponse response = customerService.createCustomer(businessId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Customer created successfully", response));
    }

    @PatchMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @PathVariable Long businessId,
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerRequest request) {
        CustomerResponse response = customerService.updateCustomer(businessId, customerId, request);
        return ResponseEntity.ok(ApiResponse.ok("Customer updated successfully", response));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<ApiResponse<Void>> archiveCustomer(
            @PathVariable Long businessId,
            @PathVariable Long customerId) {
        customerService.archiveCustomer(businessId, customerId);
        return ResponseEntity.ok(ApiResponse.ok("Customer archived successfully"));
    }
}
