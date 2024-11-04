package com.abdav.giri_guide.model.response;

import java.util.List;

public record MountainListHikingPointResponse(
                String mountainId,
                String mountainName,
                Double priceSimaksi,
                List<HikingPointResponse> hikingPoints

) {
}
