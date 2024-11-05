package com.abdav.giri_guide.model.response;

import java.util.Date;

import com.abdav.giri_guide.constant.EGender;

public record TourGuideProfileResponse(
                String userId,
                String tourGuideId,
                String email,
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
                Long pricePorter,
                String image,

                Integer totalCustomer,
                Integer donePercentage,
                Integer rejectPercentage,
                Double rating,
                Integer totalReview

) {
}
