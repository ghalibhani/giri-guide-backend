package com.abdav.giri_guide.service;

import com.abdav.giri_guide.entity.Customer;
import com.abdav.giri_guide.model.response.CustomerResponse;
import org.springframework.data.domain.Page;

public interface CustomerService {
    void createCustomer(Customer customer);
    Page<CustomerResponse> customerList(Integer page, Integer size);
    CustomerResponse getCustomerById(String id);
}
