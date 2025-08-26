package com.infosys.customerservices.service;


import com.infosys.customerservices.dto.AddPurchaseRequest;
import com.infosys.customerservices.dto.AddPurchaseResponse;
import com.infosys.customerservices.dto.GetRewardsResponse;
import com.infosys.customerservices.exception.InvalidDateException;
import com.infosys.customerservices.exception.NoDataFoundException;
import com.infosys.customerservices.model.Purchase;
import com.infosys.customerservices.repository.PurchaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomerRewardsServiceTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @InjectMocks
    private CustomerRewardsService customerRewardsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //Test for addPurchase
    @Test
    void testAddPurchase_ShouldReturnCorrectPoints() {
        AddPurchaseRequest request = new AddPurchaseRequest("CUST123", 120.0);
        when(purchaseRepository.sumPointsByCustomerId("CUST123")).thenReturn(90);

        AddPurchaseResponse response = customerRewardsService.addPurchase(request);

        assertEquals(90, response.getTotalRewardPoints()); // 70 (2x20 + 1x50 = 90)
        assertEquals(90, response.getPointsEarned());
        verify(purchaseRepository, times(1)).save(any(Purchase.class));
    }


    //Test for getRewards valid scenario
    @Test
    void testGetRewards_ShouldReturnRewards() {
        String customerId = "CUST123";
        LocalDate start = LocalDate.of(2025, 8, 1);
        LocalDate end = LocalDate.of(2025, 8, 31);

        Purchase p1 = new Purchase(1L, customerId, 120.0, LocalDate.of(2025, 8, 10), 90);
        Purchase p2 = new Purchase(2L, customerId, 220.0, LocalDate.of(2025, 8, 15), 290);
        when(purchaseRepository.findByCustomerIdAndPurchaseDateBetween(customerId, start, end))
                .thenReturn(List.of(p1, p2));

        GetRewardsResponse response = customerRewardsService.getRewards(customerId, start, end);

        assertEquals(customerId, response.getCustomerId());
        assertFalse(response.getMonthlyRewards().isEmpty(), "Monthly rewards should not be empty");
        assertEquals(380, response.getTotalRewards());
    }

    //Invalid Date Scenario
    @Test
    void testGetRewards_ShouldThrowInvalidDateException() {
        LocalDate start = LocalDate.of(2025, 9, 1);
        LocalDate end = LocalDate.of(2025, 8, 1);

        assertThrows(InvalidDateException.class, () -> {
            customerRewardsService.getRewards("CUST123", start, end);
        });

    }

    //No Data Found
    @Test
    void testGetRewards_ShouldThrowNoDataFoundException() {
        String customerId = "CUST999";
        LocalDate start = LocalDate.of(2025, 8, 1);
        LocalDate end = LocalDate.of(2025, 8, 31);

        when(purchaseRepository.findByCustomerIdAndPurchaseDateBetween(customerId, start, end))
                .thenReturn(List.of());

        assertThrows(NoDataFoundException.class, () -> {
            customerRewardsService.getRewards(customerId, start, end);
        });
    }


}
