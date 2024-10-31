package com.abdav.giri_guide.service;

import com.abdav.giri_guide.dto.request.LoginRequest;
import com.abdav.giri_guide.dto.request.RegisterRequest;
import com.abdav.giri_guide.dto.response.CommonResponse;
import com.abdav.giri_guide.dto.response.LoginResponse;

public interface AuthService {
    void register(RegisterRequest registerRequest);
    LoginResponse login(LoginRequest loginRequest);
}
