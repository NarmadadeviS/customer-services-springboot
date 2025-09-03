package com.narmada.assignment.customerservices.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String mobileNumber) {
        super("Mobile number already found: " + mobileNumber);
    }
}
