package com.abdav.giri_guide.model.response;

import com.abdav.giri_guide.constant.ETransactionStatus;

import java.util.Map;

public record CountTransactionResponse(
        Map<ETransactionStatus, Long> countTransactionPrevYear,
        Map<ETransactionStatus, Long> countTransactionPrevMonth,
        int prevYear,
        int prevMonth
) {
}
