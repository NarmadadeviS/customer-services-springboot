package com.narmada.assignment.customerservices.service;

import com.narmada.assignment.customerservices.dto.AddCustomerRequest;
import com.narmada.assignment.customerservices.dto.AddCustomerResponse;
import jakarta.transaction.Transactional;

public interface CustomerService {

    @Transactional
    AddCustomerResponse addCustomer(AddCustomerRequest request);
}
