package com.abdav.giri_guide.model.request;

import java.util.Date;

import com.abdav.giri_guide.constant.EGender;

public record TourGuideRequest(
        String email,
        String password,

        String name,
        EGender gender,
        String nik,
        Date birthDate,
        String description,
        String address,

        Integer maxHiker,
        Long price,
        Long additionalPrice,
        Integer totalPorter,
        Long pricePorter

) {
}
