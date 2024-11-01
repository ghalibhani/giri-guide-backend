package com.abdav.giri_guide.mapper;

import com.abdav.giri_guide.entity.Customer;
import com.abdav.giri_guide.model.response.CustomerResponse;

public class CustomerMapper {
    public static CustomerResponse customerToCustomerResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFullName(),
                customer.getBirthDate(),
                customer.getNik(),
                customer.getAddress(),
                customer.getGender(),
                customer.getUser().getEmail()
        );
    }
}
