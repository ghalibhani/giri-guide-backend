package com.abdav.giri_guide.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.abdav.giri_guide.model.request.TourGuideAddHikingPointRequest;
import com.abdav.giri_guide.model.request.TourGuideRequest;
import com.abdav.giri_guide.model.request.UserIdRequest;
import com.abdav.giri_guide.model.response.CommonResponseWithPage;
import com.abdav.giri_guide.model.response.TourGuideDetailResponse;
import com.abdav.giri_guide.model.response.TourGuideHikingPointActiveResponse;
import com.abdav.giri_guide.model.response.TourGuideListResponse;
import com.abdav.giri_guide.model.response.TourGuideProfileResponse;
import com.abdav.giri_guide.model.response.TourGuideStatsResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface TourGuideService {
    TourGuideProfileResponse createTourGuide(MultipartFile image, TourGuideRequest request,
            HttpServletRequest httpReq);

    TourGuideProfileResponse getTourGuideProfile(String userId, HttpServletRequest httpReq);

    TourGuideProfileResponse getTourGuideData(String id, HttpServletRequest httpReq);

    TourGuideDetailResponse getTourGuide(String id, HttpServletRequest httpReq);

    TourGuideDetailResponse updateTourGuide(String id, TourGuideRequest request, HttpServletRequest httpReq);

    TourGuideDetailResponse updateTourGuideImage(String id, MultipartFile image, HttpServletRequest httpReq);

    TourGuideDetailResponse toggleTourGuideActiveStatus(String id, HttpServletRequest httpReq);

    void softDeleteTourGuide(String id);

    CommonResponseWithPage<List<TourGuideListResponse>> getTourGuideList(
            String hikingPointId, Integer size, Integer page, HttpServletRequest httpReq);

    TourGuideDetailResponse addHikingPoint(
            String tourGuideId, TourGuideAddHikingPointRequest request, HttpServletRequest httpReq);

    List<TourGuideHikingPointActiveResponse> getTourGuideHikingPointActiveList(String userId);

    List<TourGuideHikingPointActiveResponse> toggleTourGuideHikingPointActiveList(
            String userId, String hikingPointId);

    void softDeleteTourGuideHikingPoint(String tourGuideId, String hikingPointid);

    TourGuideStatsResponse getTourGuideStats(String userId);

}
