package com.abdav.giri_guide.mapper;

import java.util.ArrayList;
import java.util.List;

import com.abdav.giri_guide.entity.TourGuide;
import com.abdav.giri_guide.entity.TourGuideHikingPoint;
import com.abdav.giri_guide.model.response.TourGuideDetailResponse;
import com.abdav.giri_guide.model.response.TourGuideListResponse;
import com.abdav.giri_guide.model.response.TourGuideProfileResponse;
import com.abdav.giri_guide.util.UrlUtil;

import jakarta.servlet.http.HttpServletRequest;

public class TourGuideMapper {
    private TourGuideMapper() {
    }

    public static TourGuideProfileResponse toTourGuideProfileResponse(
            TourGuide tourGuide,
            HttpServletRequest httpReq) {

        return new TourGuideProfileResponse(
                tourGuide.getUsers().getId(),
                tourGuide.getId(),
                tourGuide.getUsers().getEmail(),
                tourGuide.getName(),
                tourGuide.getGender(),
                tourGuide.getNik(),
                tourGuide.getBirthDate(),
                tourGuide.getDescription(),
                tourGuide.getAddress(),
                tourGuide.getMaxHiker(),
                tourGuide.getPrice(),
                tourGuide.getAdditionalPrice(),
                tourGuide.getTotalPorter(),
                tourGuide.getPricePorter(),
                (tourGuide.getImage() == null) ? null : UrlUtil.resolveImageUrl(tourGuide.getImage(), httpReq));
    }

    public static TourGuideDetailResponse toTourGuideDetailResponse(
            TourGuide tourGuide,
            List<TourGuideHikingPoint> tourGuideHikingPoint,
            HttpServletRequest httpReq

    ) {
        return new TourGuideDetailResponse(
                tourGuide.getId(),
                tourGuide.getName(),
                (tourGuide.getImage() == null) ? null : UrlUtil.resolveImageUrl(tourGuide.getImage(), httpReq),
                tourGuide.getDescription(),
                tourGuide.isActive(),
                // TODO fix this hard code
                3.3,
                5,
                10,
                tourGuide.getPrice(),
                tourGuide.getAdditionalPrice(),
                tourGuide.getTotalPorter(),
                tourGuide.getPricePorter(),
                TourGuideHikingPointMapper.toListMountainListHikingPoint(tourGuideHikingPoint)

        );
    }

    public static List<TourGuideListResponse> toListOfTourGuideListResponse(
            List<TourGuide> tourGuides, HttpServletRequest httpReq) {

        List<TourGuideListResponse> result = new ArrayList<>();
        for (TourGuide tourGuide : tourGuides) {
            result.add(new TourGuideListResponse(
                    tourGuide.getId(),
                    tourGuide.getName(),
                    (tourGuide.getImage() == null) ? null : UrlUtil.resolveImageUrl(tourGuide.getImage(), httpReq),
                    tourGuide.getDescription(),
                    5.0,
                    10));
        }
        return result;
    }
}
