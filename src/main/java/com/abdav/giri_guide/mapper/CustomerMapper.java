package com.abdav.giri_guide.mapper;

import com.abdav.giri_guide.entity.Customer;
import com.abdav.giri_guide.model.response.CustomerResponse;
import com.abdav.giri_guide.util.UrlUtil;
import jakarta.servlet.http.HttpServletRequest;

public class CustomerMapper {
    public static CustomerResponse customerToCustomerResponse(Customer customer, HttpServletRequest httpReq) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFullName(),
                customer.getBirthDate(),
                customer.getNik(),
                customer.getAddress(),
                customer.getGender(),
                customer.getUser().getEmail(),
                customer.getImage() == null ? null : UrlUtil.resolveImageUrl(customer.getImage(), httpReq),
                customer.getCreatedDate()
        );
    }
}
