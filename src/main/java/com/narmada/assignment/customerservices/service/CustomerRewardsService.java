package com.narmada.assignment.customerservices.service;

import com.narmada.assignment.customerservices.dto.*;
import com.narmada.assignment.customerservices.exception.NoDataFoundException;
import com.narmada.assignment.customerservices.exception.UserNotFoundException;
import com.narmada.assignment.customerservices.helper.RewardAggregator;
import com.narmada.assignment.customerservices.helper.RewardCalculator;
import com.narmada.assignment.customerservices.model.Customer;
import com.narmada.assignment.customerservices.model.Purchase;
import com.narmada.assignment.customerservices.repository.CustomerRepository;
import com.narmada.assignment.customerservices.repository.PurchaseRepository;
import com.narmada.assignment.customerservices.validation.CommonValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CustomerRewardsService implements CustomerRewards {

    private final PurchaseRepository purchaseRepository;
    private final CustomerRepository customerRepository;
    private final RewardAggregator rewardAggregator;
    private final RewardCalculator rewardCalculator;
    private final CommonValidator commonValidator;

    public CustomerRewardsService(PurchaseRepository purchaseRepository,
                                  CustomerRepository customerRepository,
                                  RewardAggregator rewardAggregator,
                                  RewardCalculator rewardCalculator,
                                  CommonValidator commonValidator) {
        this.purchaseRepository = purchaseRepository;
        this.customerRepository = customerRepository;
        this.rewardAggregator = rewardAggregator;
        this.rewardCalculator = rewardCalculator;
        this.commonValidator = commonValidator;
    }

    @Override
    public AddPurchaseResponse addPurchase(AddPurchaseRequest request) {
        int points = rewardCalculator.calculateRewardPoints(request.getAmount());
        boolean exists = customerRepository.existsByCustomerId(request.getCustomerId());
        if (!exists) {
            throw new UserNotFoundException(request.getCustomerId());
        }

        Purchase purchase = new Purchase();
        purchase.setCustomerId(request.getCustomerId());
        purchase.setAmount(request.getAmount());
        purchase.setPurchaseDate(LocalDate.now());
        purchase.setRewardPoints(points);
        purchaseRepository.save(purchase);

        Integer totalPoints = purchaseRepository.sumPointsByCustomerId(request.getCustomerId());
        if (totalPoints == null) totalPoints = 0;

        return new AddPurchaseResponse(points, totalPoints);
    }

    @Override
    public GetRewardsResponse getRewards(String customerId, LocalDate startDate, LocalDate endDate) {
        commonValidator.validateDate(startDate, endDate);
        Customer customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new NoDataFoundException("Customer not found"));

        List<Purchase> purchases = purchaseRepository.findByCustomerIdAndPurchaseDateBetween(customerId, startDate,
                                                                                             endDate);

        if (purchases.isEmpty()) {
            throw new NoDataFoundException(customerId);
        }

        return new GetRewardsResponse(customerId, customer.getName(),
                                      customer.getMobileNumber(),
                                      customer.getEmailId(),
                                      rewardAggregator.calculateMonthlyRewards(purchases),
                                      rewardAggregator.calculateTotalRewards(purchases),
                                      rewardAggregator.getRecentPurchases(purchases));
    }
}
