package com.abdav.giri_guide.model.response;

import java.time.LocalDateTime;
import java.util.List;

public record TransactionDetailResponse(
        String id,
        String transactionStatus,
        String mountainName,
        String hikingPointName,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Long days,
        String customerId,
        String tourGuideId,
        String tourGuideName,
        List<HikerDetailResponse> hikers,
        Integer porter,
        Long porterPerDay,
        Long totalPricePorter,
        Long tourGuidePerDay,
        Long totalPriceTourGuide,
        Long additionalPerDay,
        Long totalPriceAdditional,
        Long priceSimaksi,
        Long totalPriceSimaksi,
        Long entryPerDay,
        Long totalEntry,
        Long adminCost,
        Long totalPrice,
        String customerNote
) {
}
