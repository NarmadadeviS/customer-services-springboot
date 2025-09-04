package com.narmada.assignment.customerservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetRewardsResponse extends BaseResponse {

    private String customerId;
    private String name;
    private String mobileNumber;
    private String emailId;
    private List<MonthlyReward> monthlyRewards;
    private Integer totalRewards;
    private List<PurchaseSummary> recentPurchases;
}
