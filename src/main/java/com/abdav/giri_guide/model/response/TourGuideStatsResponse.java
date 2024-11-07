package com.abdav.giri_guide.model.response;

import java.time.LocalDateTime;

public record TourGuideStatsResponse(
        Long totalBalance,
        Long totalIncome,
        Long totalWithdraw,
        Integer hikingDone,
        Integer hikingRejected,
        LocalDateTime nextHiking,
        Integer waitingHiking,
        Integer waitingApprove) {
}
