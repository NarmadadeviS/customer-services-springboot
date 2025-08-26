package com.infosys.customerservices.controller;

import com.infosys.customerservices.dto.MonthlyReward;
import com.infosys.customerservices.service.CustomerRewardsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.infosys.customerservices.dto.AddPurchaseRequest;
import com.infosys.customerservices.dto.AddPurchaseResponse;
import com.infosys.customerservices.dto.GetRewardsResponse;

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
                    "customerId": "CUST123",
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
        List<MonthlyReward> monthlyRewards = List.of(
                new MonthlyReward("21-08-2025", 140)
        );
        GetRewardsResponse response = new GetRewardsResponse("CUST123", monthlyRewards, 140);

        LocalDate startDate = LocalDate.of(2025, 8, 20);
        LocalDate endDate = LocalDate.of(2025, 8, 22);

        Mockito.when(customerRewardsService.getRewards("CUST123", startDate, endDate))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/get-rewards/CUST123")
                        .param("startDate", "2025-08-20")
                        .param("endDate", "2025-08-22"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("CUST123"))
                .andExpect(jsonPath("$.totalRewards").value(140))
                .andExpect(jsonPath("$.monthlyRewards").isNotEmpty());
    }


}
