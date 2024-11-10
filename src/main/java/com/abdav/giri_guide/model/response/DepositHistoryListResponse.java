package com.abdav.giri_guide.model.response;

import java.time.LocalDateTime;

import com.abdav.giri_guide.constant.EDepositStatus;

public record DepositHistoryListResponse(
        String id,
        String tourGuideId,
        String tourGuideName,
        LocalDateTime createdAt,
        Long nominal,
        EDepositStatus status,
        String description,
        Long currentDeposit

) {
}
