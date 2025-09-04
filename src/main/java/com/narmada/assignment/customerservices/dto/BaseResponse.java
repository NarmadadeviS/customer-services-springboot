package com.narmada.assignment.customerservices.dto;

import lombok.Data;

@Data
public class BaseResponse {

    private final long timestamp = System.currentTimeMillis();
}
