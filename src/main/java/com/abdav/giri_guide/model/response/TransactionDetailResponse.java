package com.abdav.giri_guide.model.response;

import java.util.List;

public record TransactionDetailResponse(
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
        Double pricePorter,
        Double totalTourGuidePrice,
        Double totalAdditionalPrice,
        Double totalSimaksiPrice,
        Double totalEntryPrice,
        Double adminCost,
        Double totalPrice
) {
}
