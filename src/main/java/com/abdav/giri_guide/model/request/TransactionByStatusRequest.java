package com.abdav.giri_guide.model.request;

import java.util.List;

public record TransactionByStatusRequest(
        String userId,
        List<String> listStatus
) {
}