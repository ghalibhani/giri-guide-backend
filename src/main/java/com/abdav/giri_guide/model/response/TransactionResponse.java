package com.abdav.giri_guide.model.response;

import java.time.LocalDateTime;

public record TransactionResponse(
        String id,
        String transactionStatus,
        String mountainName,
        String hikingPointId,
        String hikingPointName,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Long days,
        String customerId,
        String customerImage,
        String customerName,
        String tourGuideId,
        String tourGuideImage,
        String tourGuideName,
        Integer porter,
        Integer hikerQty,
        Long totalPrice,
        GuideReviewResponse review) {
}
