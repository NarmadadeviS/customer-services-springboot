package com.narmada.assignment.customerservices.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCustomerRequest {

    @NotBlank(message = "Customer name cannot be empty. Kindly provide name")
    private String name;

    @NotBlank(message = "Mobile number cannot be empty. Kindly provide mobile number")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @NotBlank(message = "Email cannot be empty. Kindly provide emailId")
    @Email(message = "Email should be valid")
    private String emailId;
}
