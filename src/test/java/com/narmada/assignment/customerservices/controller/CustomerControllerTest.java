package com.narmada.assignment.customerservices.controller;

import com.narmada.assignment.customerservices.dto.*;
import com.narmada.assignment.customerservices.service.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(properties = "server.servlet.context-path=")
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerServiceImpl customerService;

    @Test
    void testAddCustomerApi_ShouldReturnSuccess() throws Exception {
        AddCustomerResponse response = new AddCustomerResponse("CUST0001", "Customer Added Successfully");

        when(customerService.addCustomer(any(AddCustomerRequest.class))).thenReturn(response);

        String jsonPayload = """
                {
                    "name": "Narmada",
                    "mobileNumber": "9876543210",
                    "emailId": "narmada@example.com"
                }
                """;

        mockMvc.perform(post("/api/v1/customers/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("CUST0001"))
                .andExpect(jsonPath("$.message").value("Customer Added Successfully"));
    }

    @Test
    void testAddCustomer_MissingFields_ShouldReturnBadRequest() throws Exception {
        String invalidRequest = """
                {
                    "name": "",
                    "mobileNumber": "",
                    "emailId": ""
                }
                """;

        mockMvc.perform(post("/api/v1/customers/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddCustomer_InvalidEmail_ShouldReturnBadRequest() throws Exception {
        String invalidRequest = """
                {
                    "name": "Narmada",
                    "mobileNumber": "9876543210",
                    "emailId": "invalidEmail"
                }
                """;

        mockMvc.perform(post("/api/v1/customers/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email should be valid"));
    }

    @Test
    void testAddCustomer_InvalidMobile_ShouldReturnBadRequest() throws Exception {
        String invalidRequest = """
                {
                    "name": "Narmada",
                    "mobileNumber": "12345",
                    "emailId": "narmada@example.com"
                }
                """;

        mockMvc.perform(post("/api/v1/customers/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Mobile number must be 10 digits"));
    }
}
