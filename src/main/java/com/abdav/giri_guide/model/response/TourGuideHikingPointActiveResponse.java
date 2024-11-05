package com.abdav.giri_guide.model.response;

public record TourGuideHikingPointActiveResponse(
        String id,
        String mountainName,
        String hikingPointName,
        Boolean isActive) {
}
