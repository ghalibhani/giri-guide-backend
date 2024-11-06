package com.abdav.giri_guide.dto.response;

import com.abdav.giri_guide.constant.ERole;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String token;
    private String UserId;
    private String email;
    private ERole role;
    private String name;
}
