package com.abdav.giri_guide.controller;

import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.constant.PathApi;
import com.abdav.giri_guide.model.response.CommonResponseWithPage;
import com.abdav.giri_guide.model.response.CustomerResponse;
import com.abdav.giri_guide.model.response.PagingResponse;
import com.abdav.giri_guide.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathApi.CUSTOMER_API)
public class CustomerController {
    private final CustomerService customerService;
    private static String message;

    @GetMapping
    public ResponseEntity<CommonResponseWithPage<?>> getCustomerList(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size
    ){
        Page<CustomerResponse> customerList = customerService.customerList(page, size);
        PagingResponse paging = new PagingResponse(page, size, customerList.getTotalPages(), customerList.getTotalElements());
        message = "Customer list" + Message.SUCCESS_FETCH;

        CommonResponseWithPage<?> response = new CommonResponseWithPage<>(message, customerList.getContent(), paging);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
