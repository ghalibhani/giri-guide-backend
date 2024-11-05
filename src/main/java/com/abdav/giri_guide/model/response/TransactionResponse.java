package com.abdav.giri_guide.model.response;

import java.time.LocalDateTime;

public record TransactionResponse(
        String id,
        String transactionStatus,
        String mountainName,
        String hikingPointId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Long days,
        String customerId,
        String tourGuideName,
        Integer porter,
        Integer hikerQty,
        Long totalPrice
) {
}
