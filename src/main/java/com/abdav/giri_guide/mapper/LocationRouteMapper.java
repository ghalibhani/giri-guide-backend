package com.abdav.giri_guide.mapper;

import java.util.ArrayList;
import java.util.List;

import com.abdav.giri_guide.entity.LocationRoute;
import com.abdav.giri_guide.entity.LocationRouteNode;
import com.abdav.giri_guide.model.request.LocationRouteNodeRequest;
import com.abdav.giri_guide.model.response.LocationRouteDetailResponse;
import com.abdav.giri_guide.model.response.LocationRouteListResponse;

public class LocationRouteMapper {
    private LocationRouteMapper() {
    }

    public static LocationRouteDetailResponse toLocationRouteDetailResponse(LocationRoute locationRoute) {
        List<LocationRouteNodeRequest> routes = new ArrayList<>();
        LocationRouteNode node = locationRoute.getStartPoint();
        while (node != null) {
            routes.add(new LocationRouteNodeRequest(
                    node.getFrom(),
                    node.getTo(),
                    node.getTransportation(),
                    node.getEstimate()));
            node = node.getNext();
        }
        return new LocationRouteDetailResponse(
                locationRoute.getId(),
                locationRoute.getTitle(),
                locationRoute.getDescription(),
                routes);
    }

    public static List<LocationRouteListResponse> toListOfLocationListResponse(List<LocationRoute> routes) {
        List<LocationRouteListResponse> result = new ArrayList<>();
        for (LocationRoute locationRoute : routes) {
            result.add(new LocationRouteListResponse(
                    locationRoute.getId(),
                    locationRoute.getTitle(),
                    locationRoute.getDescription()));
        }
        return result;
    }
}
