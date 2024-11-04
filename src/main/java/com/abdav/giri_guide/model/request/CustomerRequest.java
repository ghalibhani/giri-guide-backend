package com.abdav.giri_guide.model.request;

import com.abdav.giri_guide.constant.EGender;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record CustomerRequest(
        String id,
        @NotBlank  String fullName,
        @NotBlank  String address,
        EGender gender
) {
}
