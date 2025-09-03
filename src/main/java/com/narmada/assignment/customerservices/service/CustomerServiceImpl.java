package com.narmada.assignment.customerservices.service;


import com.narmada.assignment.customerservices.dto.AddCustomerRequest;
import com.narmada.assignment.customerservices.dto.AddCustomerResponse;
import com.narmada.assignment.customerservices.exception.UserAlreadyExistsException;
import com.narmada.assignment.customerservices.model.Customer;
import com.narmada.assignment.customerservices.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    @Override
    public AddCustomerResponse addCustomer(AddCustomerRequest request) {
        if (customerRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new UserAlreadyExistsException(request.getMobileNumber());
        }
        Customer customer = new Customer();
        customer.setEmailId(request.getEmailId());
        customer.setName(request.getName());
        customer.setMobileNumber(request.getMobileNumber());

        Customer savedCustomer = customerRepository.save(customer);

        String formattedCustomerId = String.format("CUST%04d", savedCustomer.getId());

        savedCustomer.setCustomerId(formattedCustomerId);
        return new AddCustomerResponse(formattedCustomerId, "Customer Added Successfully");
    }
}