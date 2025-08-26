package com.infosys.customerservices.service;

import com.infosys.customerservices.dto.AddPurchaseRequest;
import com.infosys.customerservices.dto.AddPurchaseResponse;
import com.infosys.customerservices.dto.GetRewardsResponse;
import com.infosys.customerservices.dto.MonthlyReward;
import com.infosys.customerservices.exception.InvalidDateException;
import com.infosys.customerservices.exception.NoDataFoundException;
import com.infosys.customerservices.model.Purchase;
import com.infosys.customerservices.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerRewardsService implements CustomerRewards {
    @Autowired
    PurchaseRepository purchaseRepository;
    private final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

    @Override
    public AddPurchaseResponse addPurchase(AddPurchaseRequest request) {
        //To Calculate reward points for current purchase
        int points = calculateRewardPoints(request.getAmount());

        Purchase purchase = new Purchase();
        purchase.setCustomerId(request.getCustomerId());
        purchase.setAmount(request.getAmount());
        purchase.setPurchaseDate(LocalDate.now());
        //purchase.setPurchaseDate(request.getPurchaseDate());
        purchase.setRewardPoints(points);
        purchaseRepository.save(purchase);

        //To Calculate total points for this customer
        Integer totalPoints = purchaseRepository.sumPointsByCustomerId(request.getCustomerId());
        if (totalPoints == null) totalPoints = 0;

        return new AddPurchaseResponse(points, totalPoints);
    }

    @Override
    public GetRewardsResponse getRewards(String customerId, LocalDate startDate, LocalDate endDate) {

        if (startDate.isAfter(endDate)) {
            throw new InvalidDateException("Invalid Start / EndDate");
        }

        List<Purchase> purchases = purchaseRepository.findByCustomerIdAndPurchaseDateBetween(customerId, startDate, endDate);

        if (purchases.isEmpty()) {
            throw new NoDataFoundException(customerId);
        }

        Map<String, Integer> monthlyPointsMap = new LinkedHashMap<>();

        int totalRewards = 0;

        for (Purchase purchase : purchases) {
            String monthKey = purchase.getPurchaseDate().format(monthFormatter);
            int points = purchase.getRewardPoints();
            monthlyPointsMap.put(monthKey, monthlyPointsMap.getOrDefault(monthKey, 0) + points);
            totalRewards += points;
        }

        List<MonthlyReward> monthlyRewards = monthlyPointsMap.entrySet().stream()
                .map(e -> new MonthlyReward(e.getKey(), e.getValue()))
                .toList();

        return new GetRewardsResponse(customerId, monthlyRewards, totalRewards);
    }

    private int calculateRewardPoints(double purchaseAmount) {
        if (purchaseAmount <= 50) {
            return 0;
        }
        double above100 = Math.max(0, purchaseAmount - 100);
        double above50 = Math.min(purchaseAmount, 100) - 50;

        return (int) (above100 * 2 + above50);
    }

}
