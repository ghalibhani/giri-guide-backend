package com.abdav.giri_guide.model.response;

public record TourGuideDetailResponse(
        String id,
        String name,
        String image,
        String description,
        boolean isActive,

        Double rating,
        Integer totalReview,
        Integer totalCustomer,

        Double price,
        Double additionalPrice,
        Integer totalPorter,
        Double pricePorter

) {
}
