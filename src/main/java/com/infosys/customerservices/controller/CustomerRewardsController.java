package com.infosys.customerservices.controller;

import com.infosys.customerservices.dto.AddPurchaseRequest;
import com.infosys.customerservices.dto.AddPurchaseResponse;
import com.infosys.customerservices.dto.GetRewardsResponse;
import com.infosys.customerservices.model.Purchase;
import com.infosys.customerservices.repository.PurchaseRepository;
import com.infosys.customerservices.service.CustomerRewardsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CustomerRewardsController {

    @Autowired
    CustomerRewardsService customerRewardsService;

    @Autowired
    PurchaseRepository purchaseRepository;

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

    //For Testing the purchase repo added this endpoint
    @GetMapping("/purchases")
    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

}
