package com.narmada.assignment.customerservices.controller;

import com.narmada.assignment.customerservices.dto.AddPurchaseRequest;
import com.narmada.assignment.customerservices.dto.AddPurchaseResponse;
import com.narmada.assignment.customerservices.dto.GetRewardsResponse;
import com.narmada.assignment.customerservices.service.CustomerRewardsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1")
public class CustomerRewardsController {

    private final CustomerRewardsService customerRewardsService;

    public CustomerRewardsController(CustomerRewardsService customerRewardsService) {
        this.customerRewardsService = customerRewardsService;
    }

    @PostMapping("/add-purchase")
    public ResponseEntity<AddPurchaseResponse> addPurchase(@Valid @RequestBody AddPurchaseRequest purchase) {
        AddPurchaseResponse response = customerRewardsService.addPurchase(purchase);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-rewards/{customerId}")
    public ResponseEntity<GetRewardsResponse> getRewardPoints(
            @PathVariable @NotBlank String customerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        GetRewardsResponse response = customerRewardsService.getRewards(customerId, startDate, endDate);
        return ResponseEntity.ok(response);
    }


}
