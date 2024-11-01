package com.abdav.giri_guide.service.impl;

import com.abdav.giri_guide.entity.Customer;
import com.abdav.giri_guide.model.response.CustomerResponse;
import com.abdav.giri_guide.repository.CustomerRepository;
import com.abdav.giri_guide.service.CustomerService;
import com.abdav.giri_guide.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public void createCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public Page<CustomerResponse> customerList(Integer page, Integer size) {
        if(page <= 0){
            page = 1;
        }
        Pageable pageable = PageRequest.of(page-1, size);
        Page <Customer> customers = customerRepository.findAllByDeletedDateIsNull(pageable);
        Page<CustomerResponse> customerResponses = customers.map(customer -> new CustomerResponse(
                customer.getId(),
                customer.getFullName(),
                customer.getBirthDate(),
                customer.getNik(),
                customer.getAddress(),
                customer.getGender(),
                customer.getUser().getEmail()
        ));
        return customerResponses;
    }
}
