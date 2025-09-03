package com.narmada.assignment.customerservices.controller;

import com.narmada.assignment.customerservices.dto.*;
import com.narmada.assignment.customerservices.service.CustomerRewardsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(properties = "server.servlet.context-path=")
@AutoConfigureMockMvc
class CustomerRewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRewardsService customerRewardsService;

    @Test
    void testAddPurchaseApi_success() throws Exception {
        AddPurchaseResponse response = new AddPurchaseResponse(90, 150);

        Mockito.when(customerRewardsService.addPurchase(Mockito.any(AddPurchaseRequest.class)))
                .thenReturn(response);

        String requestJson = """
                {
                    "customerId": "CUST0001",
                    "amount": 120.0
                }
                """;

        mockMvc.perform(post("/api/v1/add-purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pointsEarned").value(90))
                .andExpect(jsonPath("$.totalRewardPoints").value(150));
    }

    @Test
    void testGetRewardsApi_success() throws Exception {
        List<MonthlyReward> monthlyRewards = List.of(new MonthlyReward("2025-08", 140));
        List<PurchaseSummary> purchaseSummaries = List.of(
                new PurchaseSummary(LocalDate.of(2025, 8, 20), 120.0, 90)
        );

        GetRewardsResponse response = new GetRewardsResponse(
                "CUST0001",
                "Narmada",
                "9876543210",
                "narmada@example.com",
                monthlyRewards,
                140,
                purchaseSummaries
        );

        Mockito.when(customerRewardsService.getRewards("CUST0001", LocalDate.of(2025, 8, 20), LocalDate.of(2025, 8, 22)))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/get-rewards/CUST0001")
                        .param("startDate", "2025-08-20")
                        .param("endDate", "2025-08-22"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("CUST0001"))
                .andExpect(jsonPath("$.name").value("Narmada"))
                .andExpect(jsonPath("$.mobileNumber").value("9876543210"))
                .andExpect(jsonPath("$.emailId").value("narmada@example.com"))
                .andExpect(jsonPath("$.totalRewards").value(140))
                .andExpect(jsonPath("$.monthlyRewards").isNotEmpty())
                .andExpect(jsonPath("$.recentPurchases").isNotEmpty());
    }
}

