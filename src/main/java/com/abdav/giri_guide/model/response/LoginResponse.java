package com.abdav.giri_guide.model.response;

import com.abdav.giri_guide.constant.ERole;

public record LoginResponse(
        String token,
        String UserId,
        String email,
        ERole role,
        String name
) {
}
