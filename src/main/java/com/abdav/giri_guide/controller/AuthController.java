package com.abdav.giri_guide.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.constant.PathApi;
import com.abdav.giri_guide.dto.request.LoginRequest;
import com.abdav.giri_guide.dto.request.RegisterRequest;
import com.abdav.giri_guide.dto.response.CommonResponse;
import com.abdav.giri_guide.dto.response.LoginResponse;
import com.abdav.giri_guide.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathApi.AUTH_API)
public class AuthController {
    private final AuthService authService;
    private static String message;

    @PostMapping(PathApi.REGISTER_API)
    ResponseEntity<?> registerCustomer(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        message = Message.USER_CREATED;
        CommonResponse<?> response = new CommonResponse<>(message, null);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping(PathApi.LOGIN_API)
    ResponseEntity<?> loginCustomer(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        message = Message.LOGIN_SUCCESS;
        CommonResponse<?> response = new CommonResponse<>(message, loginResponse);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
