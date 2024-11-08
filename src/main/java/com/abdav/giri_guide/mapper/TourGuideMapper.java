package com.abdav.giri_guide.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.abdav.giri_guide.entity.GuideReview;
import com.abdav.giri_guide.entity.TourGuide;
import com.abdav.giri_guide.entity.TourGuideHikingPoint;
import com.abdav.giri_guide.model.response.TourGuideDetailResponse;
import com.abdav.giri_guide.model.response.TourGuideStatsResponse;
import com.abdav.giri_guide.model.response.TourGuideListResponse;
import com.abdav.giri_guide.model.response.TourGuideProfileResponse;
import com.abdav.giri_guide.util.UrlUtil;

import jakarta.servlet.http.HttpServletRequest;

public class TourGuideMapper {
    private TourGuideMapper() {
    }

    public static TourGuideProfileResponse toTourGuideProfileResponse(
            TourGuide tourGuide,
            HttpServletRequest httpReq

    ) {
        AverageRating averageRating = countAverageRating(tourGuide.getReviews());
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
                (tourGuide.getImage() == null) ? null : UrlUtil.resolveImageUrl(tourGuide.getImage(), httpReq),

                // #TODO Fix Hard Code
                999,
                50,
                50,
                averageRating.rating,
                averageRating.totalReview,
                tourGuide.getBankAccount()

        );
    }

    public static TourGuideDetailResponse toTourGuideDetailResponse(
            TourGuide tourGuide,
            List<TourGuideHikingPoint> tourGuideHikingPoint,
            HttpServletRequest httpReq

    ) {
        AverageRating averageRating = countAverageRating(tourGuide.getReviews());
        return new TourGuideDetailResponse(
                tourGuide.getId(),
                tourGuide.getName(),
                (tourGuide.getImage() == null) ? null : UrlUtil.resolveImageUrl(tourGuide.getImage(), httpReq),
                tourGuide.getDescription(),
                tourGuide.isActive(),
                averageRating.rating,
                averageRating.totalReview,

                // TODO fix this hard code
                999,
                50,
                50,
                tourGuide.getMaxHiker(),
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
            AverageRating averageRating = countAverageRating(tourGuide.getReviews());
            result.add(new TourGuideListResponse(
                    tourGuide.getId(),
                    tourGuide.getName(),
                    (tourGuide.getImage() == null) ? null : UrlUtil.resolveImageUrl(tourGuide.getImage(), httpReq),
                    tourGuide.getDescription(),
                    averageRating.rating,
                    averageRating.totalReview));
        }
        return result;
    }

    // #TODO fix hard code
    public static TourGuideStatsResponse toTourGuideStatsResponse(TourGuide tourGuide) {
        return new TourGuideStatsResponse(
                tourGuide.getDeposit().getMoney(),
                9990000L,
                9990000L,
                999,
                999,
                LocalDateTime.now(),
                999,
                999

        );
    }

    private record AverageRating(
            Double rating,
            Integer totalReview) {
    }

    private static AverageRating countAverageRating(List<GuideReview> reviews) {
        Double totalRating = 0.0;
        Integer totalReview = 0;

        for (GuideReview review : reviews) {
            if (review.getDeletedDate() == null) {
                totalRating += Double.valueOf(review.getRating());
                totalReview += 1;
            }
        }

        return new AverageRating(
                (totalReview > 0) ? totalRating / Double.valueOf(totalReview) : 0.0,
                totalReview);

    }
}
