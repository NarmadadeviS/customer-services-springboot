package com.narmada.assignment.customerservices.service;

import com.narmada.assignment.customerservices.dto.AddPurchaseRequest;
import com.narmada.assignment.customerservices.dto.AddPurchaseResponse;
import com.narmada.assignment.customerservices.dto.GetRewardsResponse;

import java.time.LocalDate;

public interface CustomerRewards {

    AddPurchaseResponse addPurchase(AddPurchaseRequest purchase);

    GetRewardsResponse getRewards(String customerId, LocalDate startDate, LocalDate endDate);
}
