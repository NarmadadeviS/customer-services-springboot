package com.narmada.assignment.customerservices.repository;

import com.narmada.assignment.customerservices.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByCustomerId(String customerId);

    Optional<Customer> findByCustomerId(String customerId);

    boolean existsByMobileNumber(String mobileNumber);

}
