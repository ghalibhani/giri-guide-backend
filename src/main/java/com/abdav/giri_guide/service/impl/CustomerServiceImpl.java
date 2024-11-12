package com.abdav.giri_guide.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import com.abdav.giri_guide.model.response.RegisterCountResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.constant.PathImage;
import com.abdav.giri_guide.entity.Customer;
import com.abdav.giri_guide.entity.ImageEntity;
import com.abdav.giri_guide.mapper.CustomerMapper;
import com.abdav.giri_guide.model.request.CustomerRequest;
import com.abdav.giri_guide.model.response.CustomerResponse;
import com.abdav.giri_guide.repository.CustomerRepository;
import com.abdav.giri_guide.service.CustomerService;
import com.abdav.giri_guide.service.ImageService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ImageService imageService;

    @Override
    public void createCustomer(Customer customer) {
        customerRepository.saveAndFlush(customer);
    }

    @Override
    public Page<CustomerResponse> customerList(Integer page, Integer size, HttpServletRequest httpReq) {
        if (page <= 0) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Customer> customers = customerRepository.findAllByDeletedDateIsNull(pageable);
        Page<CustomerResponse> customerResponses = customers
                .map(customer -> CustomerMapper.customerToCustomerResponse(customer, httpReq));

        return customerResponses;
    }

    @Override
    public CustomerResponse getCustomerById(String id, HttpServletRequest httpReq) {
        Customer customer = getCustomerByIdOrThrowNotFound(id);
        return CustomerMapper.customerToCustomerResponse(customer, httpReq);
    }

    @Override
    public void deleteCustomerById(String id) {
        Customer customer = getCustomerByIdOrThrowNotFound(id);
        customer.setDeletedDate(LocalDateTime.now());
        customerRepository.saveAndFlush(customer);
    }

    @Override
    public CustomerResponse getCustomerByUserId(String userId, HttpServletRequest httpReq) {
        Customer customer = getCustomerByUserIdOrNotFound(userId);
        return CustomerMapper.customerToCustomerResponse(customer, httpReq);
    }

    @Override
    public CustomerResponse updateCustomer(String id, CustomerRequest customerRequest, HttpServletRequest httpReq) {
        Customer customer = getCustomerByUserIdOrNotFound(id);
        customer.setFullName(customerRequest.fullName());
        customer.setAddress(customerRequest.address());
        customer.setGender(customerRequest.gender());

        customerRepository.saveAndFlush(customer);

        return CustomerMapper.customerToCustomerResponse(customer, httpReq);
    }

    @Override
    public CustomerResponse uploadProfileImage(String id, MultipartFile file, HttpServletRequest httpReq) {
        Customer customer = getCustomerByUserIdOrNotFound(id);

        ImageEntity oldImage = customer.getImage();
        ImageEntity img = imageService.create(file, PathImage.PROFILE_PICTURE, customer.getFullName());
        customer.setImage(img);
        customerRepository.saveAndFlush(customer);
        if (oldImage != null) {
            imageService.delete(oldImage);
        }

        return CustomerMapper.customerToCustomerResponse(customer, httpReq);
    }

    @Override
    public RegisterCountResponse countRegister(Integer month, Integer year) {
        List<Customer> allCustomers = customerRepository.findAllByDeletedDateIsNull();

        YearMonth findYearMonth =YearMonth.of(year, month);

        Long registerCount = allCustomers.stream()
                .filter(customer -> {
                    YearMonth registerYearMonth = YearMonth.from(customer.getCreatedDate().toLocalDate());
                    return registerYearMonth.equals(findYearMonth);
                })
                .count();
        return new RegisterCountResponse(findYearMonth, registerCount);
    }

    @Override
    public Customer getById(String id) {
        return getCustomerByUserIdOrNotFound(id);
    }

    private Customer getCustomerByUserIdOrNotFound(String userId) {
        Optional<Customer> customer = customerRepository.findByUserIdAndDeletedDateIsNull(userId);
        return customer.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Message.DATA_NOT_FOUND));
    }

    private Customer getCustomerByIdOrThrowNotFound(String id) {
        Optional<Customer> customer = customerRepository.findByIdAndDeletedDateIsNull(id);
        return customer.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Message.DATA_NOT_FOUND));
    }
}
