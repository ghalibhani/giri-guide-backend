package com.abdav.giri_guide.controller;

import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.constant.PathApi;
import com.abdav.giri_guide.model.request.CustomerRequest;
import com.abdav.giri_guide.model.response.CommonResponse;
import com.abdav.giri_guide.model.response.CustomerResponse;
import com.abdav.giri_guide.service.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathApi.PROFILE_API)
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {
    private final CustomerService customerService;
    private static String message;

    @GetMapping("/{id}")
    ResponseEntity<?> getProfile(@PathVariable String id){
        CustomerResponse customer = customerService.getCustomerByUserId(id);
        message = Message.SUCCESS_FETCH;
        CommonResponse<?> response = new CommonResponse<>(message, customer);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateProfile(@PathVariable String id, @RequestBody CustomerRequest customerRequest){
        CustomerResponse customer = customerService.updateCustomer(id, customerRequest);
        message = Message.DATA_UPDATED;
        CommonResponse<?> response = new CommonResponse<>(message, customer);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
