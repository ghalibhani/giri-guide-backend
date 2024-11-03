package com.abdav.giri_guide.model.response;

import java.time.LocalDateTime;

public record TransactionResponse(
        String id,
        String transactionStatus,
        LocalDateTime schedule,
        String customerId,
        String guideId,
        Integer porter,
        Integer hikerQty,
        Double pricePorter,
        Double totalSimaksiPrice,
        Double totalEntryPrice,
        Double totalPrice
) {
}
