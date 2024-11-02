package com.abdav.giri_guide.model.request;

import java.time.LocalDateTime;
import java.util.List;

public record TransactionRequest(
        String customerId,
        String guideId,
        Integer porterQty,
        String hikingPointId,
        LocalDateTime schedule,
        List<HikerDetailRequest> hikerDetails
) {
}
