package com.abdav.giri_guide.service.impl;

import com.abdav.giri_guide.constant.EGender;
import com.abdav.giri_guide.constant.ERole;
import com.abdav.giri_guide.constant.PathImage;
import com.abdav.giri_guide.entity.Customer;
import com.abdav.giri_guide.entity.ImageEntity;
import com.abdav.giri_guide.entity.Role;
import com.abdav.giri_guide.entity.User;
import com.abdav.giri_guide.mapper.CustomerMapper;
import com.abdav.giri_guide.model.request.CustomerRequest;
import com.abdav.giri_guide.model.response.CustomerResponse;
import com.abdav.giri_guide.model.response.RegisterCountResponse;
import com.abdav.giri_guide.repository.CustomerRepository;
import com.abdav.giri_guide.repository.ImageRepository;
import com.abdav.giri_guide.service.ImageService;
import com.abdav.giri_guide.util.UrlUtil;
import org.assertj.core.api.UrlAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;

    private CustomerRequest customerRequest;

    private ImageEntity newImage;

    private MockHttpServletRequest httpReq;

    private List<Customer> customers;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        Role role = Role.builder()
                .id("roleId1")
                .role(ERole.ROLE_CUSTOMER)
                .build();

        User user = User.builder()
                .id("userId1")
                .email("alicel@gmail.com")
                .password("password")
                .role(role)
                .build();

        ImageEntity image = ImageEntity.builder()
                .id("img1")
                .path("/image")
                .size(200L)
                .mediaType("jpeg")
                .name("alice")
                .createdDate(LocalDateTime.now())
                .build();

        newImage = ImageEntity.builder()
                .id("img2")
                .path("/newImage")
                .size(200L)
                .mediaType("jpeg")
                .name("arisu")
                .createdDate(LocalDateTime.now())
                .build();

        customer = Customer.builder()
                .id("1")
                .fullName("Alice Leimbert")
                .birthDate(new Date())
                .nik("1234567891098765")
                .address("chicago")
                .gender(EGender.MALE)
                .createdDate(LocalDateTime.now())
                .user(user)
                .image(image)
                .build();

        httpReq = new MockHttpServletRequest();

        CustomerResponse customerResponse = new CustomerResponse(
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

        customerRequest = new CustomerRequest(
                null,
                "Arisu",
                "Manhattan",
                EGender.FEMALE
        );

        Customer customer1 = Customer.builder().createdDate(LocalDateTime.of(2024, 11, 10, 0, 0, 0)).build();
        Customer customer2 = Customer.builder().createdDate(LocalDateTime.of(2024, 11, 10, 0, 0, 0)).build();
        Customer customer3 = Customer.builder().createdDate(LocalDateTime.of(2024, 9, 10, 0, 0, 0)).build();

        customers = List.of(customer1, customer2, customer3);
    }

    @Test
    void createCustomer() {
        customerService.createCustomer(customer);
        verify(customerRepository, times(1)).saveAndFlush(customer);
    }

    @Test
    void customerPage_WhenCustomerExist() {
        Pageable pageable = PageRequest.of(0,10);
        Page<Customer> customers = new PageImpl<>(List.of(customer));
        when(customerRepository.findAllByDeletedDateIsNull(pageable)).thenReturn(customers);


//        when(CustomerMapper.customerToCustomerResponse(customer, httpReq)).thenReturn(customerResponse);

        Page<CustomerResponse> result = customerService.customerList(0, 10, httpReq);
        verify(customerRepository, times(1)).findAllByDeletedDateIsNull(any(Pageable.class));
        assertEquals(1, result.getContent().size());
        assertEquals("Alice Leimbert", result.getContent().get(0).fullName());
    }

    @Test
    void customerPage_WhenCustomerEmpty() {
        Page<Customer> emptyPage = Page.empty();
        when(customerRepository.findAllByDeletedDateIsNull(any(Pageable.class))).thenReturn(emptyPage);

        Page<CustomerResponse> result = customerService.customerList(0, 10, httpReq);
        assertEquals(0, result.getContent().size());
    }

    @Test
    void getCustomerById_WhenCustomerExist_ShouldReturnCustomerResponse() {
        when(customerRepository.findByIdAndDeletedDateIsNull("1")).thenReturn(Optional.of(customer));

        CustomerResponse customerResponse = customerService.getCustomerById("1", httpReq);

        assertNotNull(customerResponse);
        assertEquals("1", customerResponse.id());
    }

    @Test
    void getCustomerById_WhenCustomerNotFound_ShouldReturnExceptionNotFound() {
        when(customerRepository.findByIdAndDeletedDateIsNull("2")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> customerService.getCustomerById("2", httpReq));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("404 NOT_FOUND \"Data Not Found\"", exception.getMessage());
    }

    @Test
    void deleteCustomerById_WhenCustomerExist_ShouldSoftDeleteCustomer() {
        when(customerRepository.findByIdAndDeletedDateIsNull("1")).thenReturn(Optional.of(customer));

        customerService.deleteCustomerById("1");

        assertNotNull(customer.getDeletedDate());
        verify(customerRepository, times(1)).saveAndFlush(customer);
    }

    @Test
    void deleteCustomerById_WhenCustomerNotFound_ShouldSoftDeleteCustomer() {
        when(customerRepository.findByIdAndDeletedDateIsNull("2")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> customerService.deleteCustomerById("2"));
        assertNull(customer.getDeletedDate());
        verify(customerRepository, never()).saveAndFlush(any());
    }

    @Test
    void getCustomerByUserId_WhenCustomerExist_ShouldReturnCustomerResponse() {
        when(customerRepository.findByUserIdAndDeletedDateIsNull("userId1")).thenReturn(Optional.of(customer));

        CustomerResponse result = customerService.getCustomerByUserId("userId1", httpReq);

        assertNotNull(result);
        assertEquals("1", result.id());
        assertEquals("Alice Leimbert", result.fullName());
        assertEquals("1234567891098765", result.nik());
        assertEquals("chicago", result.address());
        assertEquals(EGender.MALE, result.gender());
        assertEquals("alicel@gmail.com", result.email());
        assertEquals("http://localhost/image", result.imageId());
    }

    @Test
    void getCustomerByUserId_WhenCustomerNotFound_ShouldReturnResponseStatusNotFound() {
        when(customerRepository.findByUserIdAndDeletedDateIsNull("userId2")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> customerService.getCustomerByUserId("userId2", httpReq));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("404 NOT_FOUND \"Data Not Found\"", exception.getMessage());
    }

    @Test
    void updateCustomer_WhenCustomerExist_ShouldUpdateCustomerData() {
        when(customerRepository.findByUserIdAndDeletedDateIsNull("userId1")).thenReturn(Optional.of(customer));

        CustomerResponse result = customerService.updateCustomer("userId1", customerRequest, httpReq);

        assertNotNull(result);
        assertEquals("Arisu", result.fullName());
        assertEquals("Manhattan", result.address());
        assertEquals(EGender.FEMALE, result.gender());
    }

    @Test
    void updateCustomer_WhenCustomerNotFound_ShouldReturnExceptionNotFound() {
        when(customerRepository.findByUserIdAndDeletedDateIsNull("userId2")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> customerService.updateCustomer("userId2", customerRequest, httpReq));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Data Not Found", exception.getReason());
    }

    @Test
    void uploadProfileImage_WhenCustomerExist_ShouldUpdateNewProfileImage() {
        when(customerRepository.findByUserIdAndDeletedDateIsNull("userId1")).thenReturn(Optional.of(customer));
        when(imageService.create(file, PathImage.PROFILE_PICTURE, customer.getFullName())).thenReturn(newImage);

        CustomerResponse response = customerService.uploadProfileImage("userId1", file, httpReq);

        assertNotNull(response);
        assertEquals("1", response.id());
        assertEquals("http://localhost/newImage", response.imageId());
    }

    @Test
    void uploadProfileImage_WhenCustomerNotFound_ShouldUpdateNewProfileImage() {
        when(customerRepository.findByUserIdAndDeletedDateIsNull("userId2")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> customerService.uploadProfileImage("userId2", file, httpReq));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Data Not Found", exception.getReason());

        verify(imageService, never()).create(any(MultipartFile.class), anyString(), anyString());
        verify(customerRepository, never()).saveAndFlush(any(Customer.class));
    }

    @Test
    void countRegister_WhenCustomerCreatedDateAtYearMonthExist_ShouldReturnRegisterResponseNumberOfCounter() {
        int month = 11;
        int year = 2024;

        when(customerRepository.findAllByDeletedDateIsNull()).thenReturn(customers);

        RegisterCountResponse response = customerService.countRegister(month, year);

        assertNotNull(response);
        assertEquals(YearMonth.of(year, month), response.yearMonth());
        assertEquals(2, response.registerCount());
    }

    @Test
    void countRegister_WhenCustomerCreatedDateAtYearMonthNotFound_ShouldReturnRegisterResponseCounterZero() {
        int month = 1;
        int year = 2024;

        when(customerRepository.findAllByDeletedDateIsNull()).thenReturn(customers);

        RegisterCountResponse response = customerService.countRegister(month, year);

        assertNotNull(response);
        assertEquals(YearMonth.of(year, month), response.yearMonth());
        assertEquals(0, response.registerCount());
    }

    @Test
    void getById_WhenCustomerExist_ShouldReturnCustomer() {
        when(customerRepository.findByUserIdAndDeletedDateIsNull("userId1")).thenReturn(Optional.of(customer));

        Customer findCustomer = customerService.getById("userId1");

        assertNotNull(findCustomer);
        assertEquals("1", findCustomer.getId());
        verify(customerRepository, times(1)).findByUserIdAndDeletedDateIsNull(anyString());
    }

    @Test
    void getById_WhenCustomerNotFound_ShouldReturnResponseExceptionNotFound() {
        when(customerRepository.findByUserIdAndDeletedDateIsNull("userId2")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> customerService.getById("userId2"));

        assertNotNull(exception);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Data Not Found", exception.getReason());
    }
}