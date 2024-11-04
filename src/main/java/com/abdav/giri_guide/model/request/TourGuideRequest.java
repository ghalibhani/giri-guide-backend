package com.abdav.giri_guide.model.request;

public record TourGuideRequest(
        String userId,
        String name,
        String nik,
        String description,

        Integer maxHiker,
        Double price,
        Double additionalPrice,
        Integer totalPorter,
        Double pricePorter

) {
}
