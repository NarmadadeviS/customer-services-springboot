package com.narmada.assignment.customerservices.dto;

public class ErrorResponse {

    private String message;
    private final long timestamp = System.currentTimeMillis();

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

