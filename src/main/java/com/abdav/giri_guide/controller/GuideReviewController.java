package com.abdav.giri_guide.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abdav.giri_guide.model.request.GuideReviewRequest;
import com.abdav.giri_guide.model.response.CommonResponse;
import com.abdav.giri_guide.service.GuideReviewService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/guide")
public class GuideReviewController {
    private final GuideReviewService reviewService;

    @PostMapping("{id}/reviews")
    public ResponseEntity<?> createReview(@PathVariable String id, @RequestBody @Validated GuideReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>("Data Created", reviewService.createGuideReview(id, request)));
    }

    @GetMapping("{id}/reviews")
    public ResponseEntity<?> getReviewsByGuideId(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(reviewService.getGuideReviewsByGuideId(id, 1, 5));
    }

    @GetMapping("{guideId}/reviews/{reviewId}")
    public ResponseEntity<?> getReviewById(@PathVariable String guideId, @PathVariable String reviewId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>("Data Fetched", reviewService.getGuideReviewById(reviewId)));
    }

}
