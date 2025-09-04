package com.narmada.assignment.customerservices;

import com.narmada.assignment.customerservices.model.Customer;
import com.narmada.assignment.customerservices.model.Purchase;
import com.narmada.assignment.customerservices.repository.CustomerRepository;
import com.narmada.assignment.customerservices.repository.PurchaseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class CustomerServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServicesApplication.class, args);
    }

    @Bean
    CommandLineRunner init(CustomerRepository customerRepo, PurchaseRepository purchaseRepo) {
        return args -> {
            String customerId = "CUST0001";

            // Check if customer exists
            if (!customerRepo.existsByCustomerId(customerId)) {
                Customer customer = new Customer(1L, customerId, "Narmada", "9876543210", "narmada@example.com");
                customerRepo.save(customer);

                // Add sample purchases for last 3 months
                List<Purchase> purchases = List.of(
                        new Purchase(1L, customerId, 120.0, LocalDate.now().minusDays(7), 90),
                        new Purchase(2L, customerId, 75.0, LocalDate.now().minusDays(30), 25),
                        new Purchase(3L, customerId, 220.0, LocalDate.now().minusDays(60), 290),
                        new Purchase(4L, customerId, 130, LocalDate.now().minusDays(90), 110)
                );

                purchaseRepo.saveAll(purchases);
            }
        };
    }


}
