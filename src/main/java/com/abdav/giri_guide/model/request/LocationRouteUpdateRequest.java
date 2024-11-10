package com.abdav.giri_guide.model.request;

import java.util.List;

public record LocationRouteUpdateRequest(
        String title,
        String description,
        List<LocationRouteNodeRequest> routes

) {
}
