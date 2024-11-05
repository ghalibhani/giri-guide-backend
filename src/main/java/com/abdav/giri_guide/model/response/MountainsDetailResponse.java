package com.abdav.giri_guide.model.response;

import java.util.Set;

import com.abdav.giri_guide.constant.EMountainStatus;

public record MountainsDetailResponse(
        String id,
        String name,
        String image,
        String city,
        String description,
        EMountainStatus status,
        String message,
        boolean useSimaksi,
        Long priceSimaksi,
        String tips,
        String bestTime,
        Set<HikingPointResponse> hikingPoints

) {
}
