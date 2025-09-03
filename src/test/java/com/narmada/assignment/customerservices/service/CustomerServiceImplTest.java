package com.narmada.assignment.customerservices.service;

import com.narmada.assignment.customerservices.dto.AddCustomerRequest;
import com.narmada.assignment.customerservices.dto.AddCustomerResponse;
import com.narmada.assignment.customerservices.exception.UserAlreadyExistsException;
import com.narmada.assignment.customerservices.model.Customer;
import com.narmada.assignment.customerservices.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void testAddCustomer_ShouldReturnFormattedCustomerId() {
        AddCustomerRequest request = new AddCustomerRequest("Narmada", "9876543210", "narmada@example.com");

        Customer mockCustomer = new Customer();
        mockCustomer.setId(1L);  // ID returned from DB after save

        when(customerRepository.save(any(Customer.class))).thenReturn(mockCustomer);

        AddCustomerResponse response = customerService.addCustomer(request);

        assertEquals("CUST0001", response.getCustomerId());
        assertEquals("Customer Added Successfully", response.getMessage());

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testAddCustomer_ShouldThrowException_WhenMobileNumberExists() {

        AddCustomerRequest request = new AddCustomerRequest("John Doe", "9999999999", "john@example.com");

        when(customerRepository.existsByMobileNumber("9999999999")).thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () ->
                customerService.addCustomer(request));

        assertEquals("Mobile number already found: 9999999999", exception.getMessage());
        verify(customerRepository, times(1)).existsByMobileNumber("9999999999");
    }
}
