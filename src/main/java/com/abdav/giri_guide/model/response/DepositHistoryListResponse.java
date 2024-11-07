package com.abdav.giri_guide.model.response;

import java.time.LocalDateTime;

import com.abdav.giri_guide.constant.EDepositStatus;

public record DepositHistoryListResponse(
        String id,
        LocalDateTime createdAt,
        Long nominal,
        EDepositStatus status,
        String description

) {
}
