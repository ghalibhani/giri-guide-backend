package com.abdav.giri_guide.service;

import com.abdav.giri_guide.dto.request.LoginRequest;
import com.abdav.giri_guide.dto.response.LoginResponse;
import com.abdav.giri_guide.model.request.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);

    void changePassword(String userId, String oldPassword, String newPassword);
}
