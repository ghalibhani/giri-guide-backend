package com.abdav.giri_guide.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record TransactionPaymentResponse(
        String id,
        String transactionId,
        String customerId,
        Long amount,
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss a", timezone = "Asia/Jakarta")
        Date date,
        PaymentResponse paymentResponse
        ) {
}
