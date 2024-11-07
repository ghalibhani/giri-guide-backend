package com.abdav.giri_guide.model.request;

public record TransactionStatusUpdateRequest(
        String status,
        String rejectedNote
) {
}
