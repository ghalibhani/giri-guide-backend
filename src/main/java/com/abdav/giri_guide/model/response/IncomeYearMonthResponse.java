package com.abdav.giri_guide.model.response;

import java.time.YearMonth;

public record IncomeYearMonthResponse(
        YearMonth yearMonth,
        Long income
) {
}
