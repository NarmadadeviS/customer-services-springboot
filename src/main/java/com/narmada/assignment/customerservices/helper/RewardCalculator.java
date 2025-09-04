package com.narmada.assignment.customerservices.helper;


import org.springframework.stereotype.Component;

@Component
public class RewardCalculator {

    public int calculateRewardPoints(double purchaseAmount) {
        if (purchaseAmount <= 50) return 0;

        double above100 = Math.max(0, purchaseAmount - 100);
        double above50 = Math.min(purchaseAmount, 100) - 50;

        return (int) (above100 * 2 + above50);
    }
}
