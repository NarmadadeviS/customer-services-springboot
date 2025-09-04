package com.narmada.assignment.customerservices.validation;


import com.narmada.assignment.customerservices.exception.InvalidDateException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CommonValidator {

    public void validateDate(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateException("Invalid Start / EndDate");
        }
    }

}
