package com.abdav.giri_guide.service;

import com.abdav.giri_guide.entity.Customer;
import com.abdav.giri_guide.model.request.CustomerRequest;
import com.abdav.giri_guide.model.response.CustomerResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface CustomerService {
    void createCustomer(Customer customer);
    Page<CustomerResponse> customerList(Integer page, Integer size, HttpServletRequest httpReq);
    CustomerResponse getCustomerById(String id, HttpServletRequest httpReq);
    void deleteCustomerById(String id);
    CustomerResponse getCustomerByUserId(String userId, HttpServletRequest httpReq);
    CustomerResponse updateCustomer(String id, CustomerRequest customerRequest, HttpServletRequest httpReq);
    CustomerResponse uploadProfileImage(String id, MultipartFile file, HttpServletRequest httpReq);
}
