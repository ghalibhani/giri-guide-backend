package com.abdav.giri_guide.service;

import java.util.List;

import com.abdav.giri_guide.entity.GuideReview;
import com.abdav.giri_guide.entity.Transaction;
import com.abdav.giri_guide.model.request.GuideReviewPutRequest;
import com.abdav.giri_guide.model.request.GuideReviewRequest;
import com.abdav.giri_guide.model.response.CommonResponseWithPage;
import com.abdav.giri_guide.model.response.GuideReviewResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface GuideReviewService {
    GuideReviewResponse createGuideReview(String userId, GuideReviewRequest request, HttpServletRequest httpReq);

    GuideReviewResponse getGuideReviewById(String id, HttpServletRequest httpReq);

    CommonResponseWithPage<List<GuideReviewResponse>> getGuideReviewsByGuideId(
            String tourGuideId, Integer page, Integer size, HttpServletRequest httpReq);

    GuideReview createBlankReview(Transaction transaction);

    GuideReviewResponse putReviewOnTransaction(
            String transactionId, GuideReviewPutRequest request, HttpServletRequest httpReq);

    GuideReview getGuideReviewByTransactionId(String transactionId);
}
