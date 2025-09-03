package com.narmada.assignment.customerservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCustomerResponse extends BaseResponse {
        private String customerId;
        private String message;
}
