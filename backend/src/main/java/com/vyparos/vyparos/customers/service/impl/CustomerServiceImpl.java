package com.vyparos.vyparos.customers.service.impl;

import com.vyparos.vyparos.business.entity.BusinessMembership;
import com.vyparos.vyparos.common.dto.PageResponse;
import com.vyparos.vyparos.customers.dto.CustomerRequest;
import com.vyparos.vyparos.customers.dto.CustomerResponse;
import com.vyparos.vyparos.customers.entity.Customer;
import com.vyparos.vyparos.customers.repository.CustomerRepository;
import com.vyparos.vyparos.customers.service.CustomerService;
import com.vyparos.vyparos.exception.ResourceNotFoundException;
import com.vyparos.vyparos.security.TenantSecurityGuard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final TenantSecurityGuard securityGuard;

    public CustomerServiceImpl(CustomerRepository customerRepository, TenantSecurityGuard securityGuard) {
        this.customerRepository = customerRepository;
        this.securityGuard = securityGuard;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CustomerResponse> getCustomers(Long businessId, String query, Pageable pageable) {
        securityGuard.requireMembership(businessId);
        Page<Customer> customers = customerRepository.searchCustomers(businessId, query, pageable);
        Page<CustomerResponse> responsePage = customers.map(CustomerResponse::fromEntity);
        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long businessId, Long customerId) {
        securityGuard.requireMembership(businessId);
        Customer customer = customerRepository.findByIdAndBusinessId(customerId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found in this business workspace"));
        return CustomerResponse.fromEntity(customer);
    }

    @Override
    @Transactional
    public CustomerResponse createCustomer(Long businessId, CustomerRequest request) {
        BusinessMembership membership = securityGuard.requireMembership(businessId);

        Customer customer = new Customer(
                membership.getBusiness(),
                request.getName().trim(),
                request.getPhone() != null ? request.getPhone().trim() : null,
                request.getEmail() != null ? request.getEmail().trim() : null,
                request.getAddress() != null ? request.getAddress().trim() : null,
                request.getNotes() != null ? request.getNotes().trim() : null
        );

        Customer saved = customerRepository.save(customer);
        return CustomerResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(Long businessId, Long customerId, CustomerRequest request) {
        securityGuard.requireMembership(businessId);

        Customer customer = customerRepository.findByIdAndBusinessId(customerId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found in this business workspace"));

        if (request.getName() != null && !request.getName().isBlank()) {
            customer.setName(request.getName().trim());
        }
        if (request.getPhone() != null) {
            customer.setPhone(request.getPhone().trim());
        }
        if (request.getEmail() != null) {
            customer.setEmail(request.getEmail().trim());
        }
        if (request.getAddress() != null) {
            customer.setAddress(request.getAddress().trim());
        }
        if (request.getNotes() != null) {
            customer.setNotes(request.getNotes().trim());
        }

        Customer saved = customerRepository.save(customer);
        return CustomerResponse.fromEntity(saved);
    }

    @Override
    @Transactional
    public void archiveCustomer(Long businessId, Long customerId) {
        securityGuard.requireAdminOrOwner(businessId);

        Customer customer = customerRepository.findByIdAndBusinessId(customerId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found in this business workspace"));

        customer.setArchived(true);
        customerRepository.save(customer);
    }
}
