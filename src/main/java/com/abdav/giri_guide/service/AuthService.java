package com.abdav.giri_guide.service;

import com.abdav.giri_guide.model.request.LoginRequest;
import com.abdav.giri_guide.model.request.RegisterRequest;
import com.abdav.giri_guide.model.response.LoginResponse;

public interface AuthService {
    void register(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);

    void changePassword(String userId, String oldPassword, String newPassword);
}
