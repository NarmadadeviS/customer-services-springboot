package com.infosys.customerservices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetRewardsResponse extends BaseResponse {
    private String customerId;
    private List<MonthlyReward> monthlyRewards;
    private Integer totalRewards;
}
