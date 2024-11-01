package com.abdav.giri_guide.model.request;

import com.abdav.giri_guide.constant.EGender;

import java.util.Date;

public record CustomerRequest(
        String id,
        String fullName,
        String address,
        EGender gender
) {
}
