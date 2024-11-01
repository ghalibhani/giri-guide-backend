package com.abdav.giri_guide.service;

import java.util.List;

import com.abdav.giri_guide.model.request.GuideReviewRequest;
import com.abdav.giri_guide.model.response.CommonResponseWithPage;
import com.abdav.giri_guide.model.response.GuideReviewResponse;

public interface GuideReviewService {
    GuideReviewResponse createGuideReview(String guideId, GuideReviewRequest request);

    GuideReviewResponse getGuideReviewById(String id);

    CommonResponseWithPage<List<GuideReviewResponse>> getGuideReviewsByGuideId(String id, Integer page, Integer size);
}
