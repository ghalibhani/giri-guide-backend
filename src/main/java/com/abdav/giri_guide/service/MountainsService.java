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

import jakarta.servlet.http.HttpServletRequest;

public interface MountainsService {
    CommonResponseWithPage<List<MountainsListResponse>> mountainList(
            String name, String city, Integer page, Integer size, HttpServletRequest httpReq);

    MountainsDetailResponse mountainDetail(String id, HttpServletRequest httpReq);

    MountainsDetailResponse updateMountain(String id, MountainsRequest updatedMountains, HttpServletRequest httpReq);

    MountainsDetailResponse createMountain(MountainsRequest mountains, MultipartFile image, HttpServletRequest httpReq);

    MountainsDetailResponse updateMountainImage(String id, MultipartFile image, HttpServletRequest httpRequest);

    void deleteMountain(String id);

    HikingPointResponse createHikingPoint(String mountainId, HikingPointRequest request);

    Set<HikingPointResponse> getHikingPoints(String mountainId);

    HikingPointResponse getHikingPoint(String id);

    HikingPointResponse updateHikingPoint(String id, HikingPointRequest request);

    void deleteHikingPoint(String id);
}
