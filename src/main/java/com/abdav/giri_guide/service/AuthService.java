package com.abdav.giri_guide.service;

import com.abdav.giri_guide.dto.request.RegisterRequest;
import com.abdav.giri_guide.dto.response.CommonResponse;

public interface AuthService {
    void register(RegisterRequest registerRequest);
}
