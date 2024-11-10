package com.abdav.giri_guide.service;

import java.util.List;

import com.abdav.giri_guide.model.request.LocationRouteRequest;
import com.abdav.giri_guide.model.request.LocationRouteUpdateRequest;
import com.abdav.giri_guide.model.response.CommonResponseWithPage;
import com.abdav.giri_guide.model.response.LocationRouteDetailResponse;
import com.abdav.giri_guide.model.response.LocationRouteListResponse;

public interface LocationRouteService {
    LocationRouteDetailResponse createRoute(LocationRouteRequest request);

    CommonResponseWithPage<List<LocationRouteListResponse>> getAllRoute(String title, Integer page, Integer size);

    LocationRouteDetailResponse getRouteDetail(String locationRouteId);

    LocationRouteDetailResponse updateRoute(String locationRouteId, LocationRouteUpdateRequest request);

    void deleteRoute(String locationRouteId);
}
