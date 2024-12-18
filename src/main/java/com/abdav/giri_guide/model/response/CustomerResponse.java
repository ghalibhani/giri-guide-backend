package com.abdav.giri_guide.model.response;

import com.abdav.giri_guide.constant.EGender;

import java.time.LocalDateTime;
import java.util.Date;

public record CustomerResponse(
        String id,
        String fullName,
        Date birthDate,
        String nik,
        String address,
        EGender gender,
        String email,
        String imageId,
        LocalDateTime createdAt
) {
}
