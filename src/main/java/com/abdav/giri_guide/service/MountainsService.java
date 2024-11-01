package com.abdav.giri_guide.service;

import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.abdav.giri_guide.model.request.HikingPointRequest;
import com.abdav.giri_guide.model.request.MountainsRequest;
import com.abdav.giri_guide.model.response.CommonResponseWithPage;
import com.abdav.giri_guide.model.response.HikingPointResponse;
import com.abdav.giri_guide.model.response.MountainsDetailResponse;
import com.abdav.giri_guide.model.response.MountainsListResponse;

public interface MountainsService {
    CommonResponseWithPage<List<MountainsListResponse>> mountainList(String city, Integer page, Integer size);

    MountainsDetailResponse mountainDetail(String id);

    MountainsDetailResponse updateMountain(String id, MountainsRequest updatedMountains);

    MountainsDetailResponse createMountain(MountainsRequest mountains, MultipartFile image);

    void deleteMountain(String id);

    HikingPointResponse createHikingPoint(String mountainId, HikingPointRequest request);

    Set<HikingPointResponse> getHikingPoints(String mountainId);

    HikingPointResponse getHikingPoint(String id);

    HikingPointResponse updateHikingPoint(String id, HikingPointRequest request);

    void deleteHikingPoint(String id);
}
