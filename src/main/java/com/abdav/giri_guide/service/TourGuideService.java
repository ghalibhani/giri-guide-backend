package com.abdav.giri_guide.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.abdav.giri_guide.model.request.TourGuideAddHikingPointRequest;
import com.abdav.giri_guide.model.request.TourGuideRequest;
import com.abdav.giri_guide.model.response.CommonResponseWithPage;
import com.abdav.giri_guide.model.response.TourGuideDetailResponse;
import com.abdav.giri_guide.model.response.TourGuideListResponse;

public interface TourGuideService {
    TourGuideDetailResponse createTourGuide(MultipartFile image, TourGuideRequest request);

    TourGuideDetailResponse getTourGuide(String id);

    TourGuideDetailResponse updateTourGuide(String id, TourGuideRequest request);

    TourGuideDetailResponse updateTourGuideImage(String id, MultipartFile image);

    TourGuideDetailResponse toggleTourGuideActiveStatus(String id);

    void softDeleteTourGuide(String id);

    CommonResponseWithPage<List<TourGuideListResponse>> getTourGuideList(String hikingPointId);

    TourGuideDetailResponse addHikingPoint(String tourGuideId, TourGuideAddHikingPointRequest request);
}
