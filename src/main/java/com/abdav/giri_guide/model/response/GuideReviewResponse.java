package com.abdav.giri_guide.model.response;

import java.time.LocalDateTime;

public record GuideReviewResponse(
        String id,
        String customerId,
        String customerName,
        LocalDateTime createdAt,
        Boolean usePorter,
        Integer rating,
        String review,
        String customerImage) {
}
