package com.abdav.giri_guide.model.response;

import java.util.List;

public record TransactionDetailResponse(
        String id,
        String transactionStatus,
        String mountainName,
        String hikingPointName,
        Long days,
        String customerId,
        String customerName,
        String tourGuideId,
        String tourGuideName,
        String imageTourGuide,
        Integer hikerQty,
        List<HikerDetailResponse> hikers,
        Integer porter,
        Long pricePorter,
        Long totalTourGuidePrice,
        Long totalAdditionalPrice,
        Long totalSimaksiPrice,
        Long totalEntryPrice,
        Long adminCost,
        Long totalPrice
) {
}
