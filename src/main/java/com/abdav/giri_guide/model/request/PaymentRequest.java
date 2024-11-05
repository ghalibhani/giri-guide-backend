package com.abdav.giri_guide.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PaymentRequest(
        @JsonProperty("transaction_details") PaymentDetailRequest paymentDetailRequest,
        @JsonProperty("enabled_payment") List<String> paymentMethod
        ) {
}
