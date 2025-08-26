package com.infosys.customerservices.repository;

import com.infosys.customerservices.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query("SELECT SUM(p.rewardPoints) FROM Purchase p WHERE p.customerId = :customerId")
    Integer sumPointsByCustomerId(@Param("customerId") String customerId);

    List<Purchase> findByCustomerIdAndPurchaseDateBetween(String customerId, LocalDate startDate, LocalDate endDate);

}
