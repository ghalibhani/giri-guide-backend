package com.abdav.giri_guide.mapper;

import com.abdav.giri_guide.entity.GuideReview;
import com.abdav.giri_guide.model.response.GuideReviewResponse;
import com.abdav.giri_guide.util.UrlUtil;

import jakarta.servlet.http.HttpServletRequest;

public class GuideReviewMapper {
    private GuideReviewMapper() {
    }

    public static GuideReviewResponse toGuideReviewResponse(GuideReview review, HttpServletRequest httpReq) {

        return new GuideReviewResponse(
                review.getId(),
                review.getCustomer().getId(),
                review.getCustomer().getFullName(),
                review.getCreatedDate(),
                review.getUsePorter(),
                review.getRating(),
                review.getReview(),
                (review.getCustomer().getImage() == null)
                        ? null
                        : UrlUtil.resolveImageUrl(review.getCustomer().getImage(), httpReq));

    }

}
