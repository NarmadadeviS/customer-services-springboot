package com.infosys.customerservices.service;

import com.infosys.customerservices.dto.AddPurchaseRequest;
import com.infosys.customerservices.dto.AddPurchaseResponse;
import com.infosys.customerservices.dto.GetRewardsResponse;

import java.time.LocalDate;

public interface CustomerRewards {
    AddPurchaseResponse addPurchase(AddPurchaseRequest purchase);

    GetRewardsResponse getRewards(String customerId, LocalDate startDate, LocalDate endDate);
}
