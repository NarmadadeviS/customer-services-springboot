package com.narmada.assignment.customerservices.helper;

import com.narmada.assignment.customerservices.dto.MonthlyReward;
import com.narmada.assignment.customerservices.dto.PurchaseSummary;
import com.narmada.assignment.customerservices.model.Purchase;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class RewardAggregator {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

    public List<MonthlyReward> calculateMonthlyRewards(List<Purchase> purchases) {

        Map<String, Integer> monthlyPointsMap = new LinkedHashMap<>();


        for (Purchase purchase : purchases) {
            String monthKey = purchase.getPurchaseDate().format(formatter);
            int points = purchase.getRewardPoints();
            monthlyPointsMap.put(monthKey, monthlyPointsMap.getOrDefault(monthKey, 0) + points);
        }

        return monthlyPointsMap.entrySet().stream()
                .map(e -> new MonthlyReward(e.getKey(), e.getValue()))
                .toList();
    }

    public int calculateTotalRewards(List<Purchase> purchases) {
        return purchases.stream().mapToInt(Purchase::getRewardPoints).sum();
    }

    public List<PurchaseSummary> getRecentPurchases(List<Purchase> purchases) {
        return purchases.stream()
                .map(p -> new PurchaseSummary(p.getPurchaseDate(), p.getAmount(), p.getRewardPoints()))
                .toList();
    }
}
