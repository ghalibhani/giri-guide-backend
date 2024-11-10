package com.abdav.giri_guide.model.response;

import java.time.YearMonth;
import java.util.Map;

public record RevenueResponse(
    Map<YearMonth, Long> revenueOneYearBefore
) {
}
