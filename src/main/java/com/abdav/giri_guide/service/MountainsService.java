package com.abdav.giri_guide.service;

import java.util.Set;
import org.springframework.data.domain.Page;

import com.abdav.giri_guide.model.request.HikingPointRequest;
import com.abdav.giri_guide.model.request.MountainsRequest;
import com.abdav.giri_guide.model.response.HikingPointResponse;
import com.abdav.giri_guide.model.response.MountainsDetailResponse;
import com.abdav.giri_guide.model.response.MountainsListResponse;

public interface MountainsService {
    Page<MountainsListResponse> mountainList(String city, Integer page, Integer size);

    MountainsDetailResponse mountainDetail(String id);

    MountainsDetailResponse updateMountain(String id, MountainsRequest updatedMountains);

    MountainsDetailResponse createMountain(MountainsRequest mountains);

    void deleteMountain(String id);

    HikingPointResponse createHikingPoint(String mountainId, HikingPointRequest request);

    Set<HikingPointResponse> getHikingPoints(String mountainId);

    HikingPointResponse getHikingPoint(String id);

    HikingPointResponse updateHikingPoint(String id, HikingPointRequest request);

    void deleteHikingPoint(String id);
}
