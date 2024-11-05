package com.abdav.giri_guide.model.response;

public record PaymentResponse(
        String id,
        String token,
        String redirectUrl,
        String transactionStatus
) {
}
