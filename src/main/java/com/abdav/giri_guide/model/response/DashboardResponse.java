package com.abdav.giri_guide.model.response;

public record DashboardResponse(
        Long totalIncome,
        IncomeYearMonthResponse totalIncomeMonth,
        RegisterCountResponse registerCount
) {
}
