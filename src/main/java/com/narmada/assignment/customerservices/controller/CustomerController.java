package com.narmada.assignment.customerservices.controller;


import com.narmada.assignment.customerservices.dto.AddCustomerRequest;
import com.narmada.assignment.customerservices.dto.AddCustomerResponse;
import com.narmada.assignment.customerservices.service.CustomerServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerServiceImpl customerService;

    public CustomerController(CustomerServiceImpl customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/add")
    public ResponseEntity<AddCustomerResponse> addCustomer(@Valid @RequestBody AddCustomerRequest request) {
        AddCustomerResponse savedCustomer = customerService.addCustomer(request);
        return ResponseEntity.ok(savedCustomer);
    }
}
