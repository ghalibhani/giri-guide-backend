package com.abdav.giri_guide.service.impl;

import com.abdav.giri_guide.entity.Customer;
import com.abdav.giri_guide.repository.CustomerRepository;
import com.abdav.giri_guide.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
}
