package com.abdav.giri_guide.model.response;

import java.util.List;

import com.abdav.giri_guide.model.request.LocationRouteNodeRequest;

public record LocationRouteDetailResponse(
        String id,
        String title,
        String description,
        List<LocationRouteNodeRequest> routes) {
}
