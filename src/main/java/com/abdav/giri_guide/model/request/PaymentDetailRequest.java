package com.abdav.giri_guide.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentDetailRequest(
        @JsonProperty("order_id") String orderId,
        @JsonProperty("gross_amount") Long amount
) {
}
