package com.narmada.assignment.customerservices.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String customerId) {
        super("Customer not found with ID: " + customerId);
    }
}