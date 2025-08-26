package com.infosys.customerservices.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddPurchaseResponse extends BaseResponse {
    private int pointsEarned;
    private int totalRewardPoints;
}