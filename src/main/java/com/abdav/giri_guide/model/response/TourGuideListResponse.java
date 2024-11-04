package com.abdav.giri_guide.model.response;

public record TourGuideListResponse(
        String id,
        String name,
        String image,
        String description,

        Double rating,
        Integer totalReview

) {
}
