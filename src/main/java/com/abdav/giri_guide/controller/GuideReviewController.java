package com.abdav.giri_guide.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.model.request.GuideReviewPutRequest;
import com.abdav.giri_guide.model.request.GuideReviewRequest;
import com.abdav.giri_guide.model.response.CommonResponse;
import com.abdav.giri_guide.service.GuideReviewService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/guide")
public class GuideReviewController {
    private final GuideReviewService reviewService;

    @PostMapping("{tourGuideId}/reviews")
    public ResponseEntity<?> createReview(
            @PathVariable String tourGuideId,
            @RequestBody @Validated GuideReviewRequest request,
            HttpServletRequest httpReq) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(
                        Message.DATA_CREATED,
                        reviewService.createGuideReview(tourGuideId, request, httpReq)));
    }

    @GetMapping("{tourGuideId}/reviews")
    public ResponseEntity<?> getReviewsByGuideId(
            @PathVariable String tourGuideId,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            HttpServletRequest httpReq

    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(reviewService.getGuideReviewsByGuideId(tourGuideId, page, size, httpReq));
    }

    @GetMapping("reviews/{reviewId}")
    public ResponseEntity<?> getReviewById(
            @PathVariable String guideId,
            @PathVariable String reviewId,
            HttpServletRequest httpReq

    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>("Data Fetched",
                        reviewService.getGuideReviewById(reviewId, httpReq)));
    }

    @PutMapping("reviews/{transactionId}")
    public ResponseEntity<?> putReview(
            @PathVariable String transactionId,
            @RequestBody GuideReviewPutRequest request,
            HttpServletRequest httpReq

    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new CommonResponse<>(
                        Message.DATA_UPDATED,
                        reviewService.putReviewOnTransaction(transactionId, request, httpReq)));
    }

}
