package com.narmada.assignment.customerservices.exception;

public class NoDataFoundException extends RuntimeException {

    public NoDataFoundException(String customerId) {
        super("No data found for customer with ID: " + customerId);
    }
}