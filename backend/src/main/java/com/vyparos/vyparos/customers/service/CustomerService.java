package com.vyparos.vyparos.customers.service;

import com.vyparos.vyparos.common.dto.PageResponse;
import com.vyparos.vyparos.customers.dto.CustomerRequest;
import com.vyparos.vyparos.customers.dto.CustomerResponse;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    PageResponse<CustomerResponse> getCustomers(Long businessId, String query, Pageable pageable);
    CustomerResponse getCustomerById(Long businessId, Long customerId);
    CustomerResponse createCustomer(Long businessId, CustomerRequest request);
    CustomerResponse updateCustomer(Long businessId, Long customerId, CustomerRequest request);
    void archiveCustomer(Long businessId, Long customerId);
}
