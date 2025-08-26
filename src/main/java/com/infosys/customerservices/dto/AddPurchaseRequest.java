package com.infosys.customerservices.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPurchaseRequest {
    @NotNull(message = "Customer ID is required")
    private String customerId;

    @Min(value = 1, message = "Amount should not be zero")
    private double amount;

//    @NotNull(message = "Purchase date is required")
//    private LocalDate purchaseDate;
}
