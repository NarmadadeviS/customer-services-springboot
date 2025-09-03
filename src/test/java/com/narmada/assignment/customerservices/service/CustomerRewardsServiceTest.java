package com.narmada.assignment.customerservices.service;


import com.narmada.assignment.customerservices.dto.*;
import com.narmada.assignment.customerservices.exception.InvalidDateException;
import com.narmada.assignment.customerservices.exception.NoDataFoundException;
import com.narmada.assignment.customerservices.helper.RewardAggregator;
import com.narmada.assignment.customerservices.helper.RewardCalculator;
import com.narmada.assignment.customerservices.model.Customer;
import com.narmada.assignment.customerservices.model.Purchase;
import com.narmada.assignment.customerservices.repository.CustomerRepository;
import com.narmada.assignment.customerservices.repository.PurchaseRepository;
import com.narmada.assignment.customerservices.validation.CommonValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerRewardsServiceTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private RewardCalculator rewardCalculator;
    @Mock
    private RewardAggregator rewardAggregator;
    @Mock
    private CommonValidator commonValidator;

    @InjectMocks
    private CustomerRewardsService customerRewardsService;

    @Test
    void testAddPurchase_ShouldReturnCorrectPoints() {
        AddPurchaseRequest request = new AddPurchaseRequest("CUST0001", 120.0);

        when(customerRepository.existsByCustomerId("CUST0001")).thenReturn(true);
        when(rewardCalculator.calculateRewardPoints(120.0)).thenReturn(90);
        when(purchaseRepository.sumPointsByCustomerId("CUST0001")).thenReturn(150);

        AddPurchaseResponse response = customerRewardsService.addPurchase(request);

        assertEquals(90, response.getPointsEarned());
        assertEquals(150, response.getTotalRewardPoints());
        verify(purchaseRepository, times(1)).save(any(Purchase.class));
    }

    @Test
    void testGetRewards_ShouldReturnRewards() {
        String customerId = "CUST0001";
        LocalDate start = LocalDate.of(2025, 8, 1);
        LocalDate end = LocalDate.of(2025, 8, 31);

        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setName("Narmada");
        customer.setMobileNumber("9876543210");
        customer.setEmailId("narmada@example.com");

        List<Purchase> purchases = List.of(
                new Purchase(1L, customerId, 120.0, LocalDate.of(2025, 8, 10), 90),
                new Purchase(2L, customerId, 220.0, LocalDate.of(2025, 8, 15), 290)
        );

        when(customerRepository.findByCustomerId(customerId)).thenReturn(Optional.of(customer));
        when(purchaseRepository.findByCustomerIdAndPurchaseDateBetween(customerId, start, end))
                .thenReturn(purchases);
        when(rewardAggregator.calculateMonthlyRewards(purchases)).thenReturn(List.of(
                new MonthlyReward("2025-08", 380)
        ));
        when(rewardAggregator.calculateTotalRewards(purchases)).thenReturn(380);
        when(rewardAggregator.getRecentPurchases(purchases)).thenReturn(List.of(
                new PurchaseSummary(LocalDate.of(2025, 8, 10), 120.0, 90),
                new PurchaseSummary(LocalDate.of(2025, 8, 15), 220.0, 290)
        ));

        GetRewardsResponse response = customerRewardsService.getRewards(customerId, start, end);

        assertEquals(customerId, response.getCustomerId());
        assertEquals(380, response.getTotalRewards());
        assertEquals("Narmada", response.getName());
        assertFalse(response.getMonthlyRewards().isEmpty());
        assertFalse(response.getRecentPurchases().isEmpty());
    }

    @Test
    void testGetRewards_ShouldThrowInvalidDateException() {
        LocalDate start = LocalDate.of(2025, 9, 1);
        LocalDate end = LocalDate.of(2025, 8, 1);

        doThrow(new InvalidDateException("Invalid date range"))
                .when(commonValidator).validateDate(start, end);

        assertThrows(InvalidDateException.class, () ->
                customerRewardsService.getRewards("CUST0001", start, end)
        );
    }

    @Test
    void testGetRewards_ShouldThrowNoDataFoundException_WhenCustomerNotFound() {
        when(customerRepository.findByCustomerId("CUST404"))
                .thenReturn(Optional.empty());

        assertThrows(NoDataFoundException.class, () ->
                customerRewardsService.getRewards("CUST404", LocalDate.now(), LocalDate.now())
        );
    }

    @Test
    void testGetRewards_ShouldThrowNoDataFoundException_WhenNoPurchases() {
        String customerId = "CUST0001";
        LocalDate start = LocalDate.of(2025, 8, 1);
        LocalDate end = LocalDate.of(2025, 8, 31);

        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setName("Narmada");

        when(customerRepository.findByCustomerId(customerId)).thenReturn(Optional.of(customer));
        when(purchaseRepository.findByCustomerIdAndPurchaseDateBetween(customerId, start, end))
                .thenReturn(List.of());

        assertThrows(NoDataFoundException.class, () ->
                customerRewardsService.getRewards(customerId, start, end)
        );
    }
}
