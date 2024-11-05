package com.abdav.giri_guide.model.response;

import com.abdav.giri_guide.constant.EMountainStatus;

public record MountainsListResponse(
        String id,
        String name,
        String image,
        String city,
        EMountainStatus status,
        Integer hikingPointCount

) {
}
