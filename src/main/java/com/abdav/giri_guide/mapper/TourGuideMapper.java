package com.abdav.giri_guide.mapper;

import java.util.List;

import com.abdav.giri_guide.entity.TourGuide;
import com.abdav.giri_guide.entity.TourGuideHikingPoint;
import com.abdav.giri_guide.model.response.TourGuideDetailResponse;
import com.abdav.giri_guide.model.response.TourGuideProfileResponse;
import com.abdav.giri_guide.util.UrlUtil;

import jakarta.servlet.http.HttpServletRequest;

public class TourGuideMapper {
    private TourGuideMapper() {
    }

    public static TourGuideProfileResponse toTourGuideProfileResponse(
            TourGuide tourGuide, HttpServletRequest httpReq) {
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
            List<TourGuideHikingPoint> tourGuideHikingPoint

    ) {
        return new TourGuideDetailResponse(
                tourGuide.getId(),
                tourGuide.getName(),
                tourGuide.getImage().getPath(),
                tourGuide.getDescription(),
                tourGuide.isActive(),
                // TODO fix this hard code
                0.0,
                200,
                10,
                tourGuide.getPrice(),
                tourGuide.getAdditionalPrice(),
                tourGuide.getTotalPorter(),
                tourGuide.getPricePorter(),
                TourGuideHikingPointMapper.toListMountainListHikingPoint(tourGuideHikingPoint)

        );
    }
}
