package com.infosys.customerservices.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class BaseResponse {
    private final long timestamp = System.currentTimeMillis();

}
