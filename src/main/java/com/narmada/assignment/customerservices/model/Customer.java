package com.narmada.assignment.customerservices.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "customer_id", unique = true)
    private String customerId;
    private String name;
    private String mobileNumber;
    private String emailId;
}
