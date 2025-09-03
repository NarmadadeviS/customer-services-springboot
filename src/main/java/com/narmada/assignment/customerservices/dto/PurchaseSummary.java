package com.narmada.assignment.customerservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseSummary {
    private LocalDate purchaseDate;
    private double amount;
    private int rewardPoints;

}
