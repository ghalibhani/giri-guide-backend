package com.abdav.giri_guide.model.response;

import java.util.List;

public record MountainListHikingPointResponse(
        String mountainId,
        String mountainName,
        Long priceSimaksi,
        List<HikingPointResponse> hikingPoints

) {
}
