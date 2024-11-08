package com.abdav.giri_guide.mapper;

import java.util.ArrayList;
import java.util.List;

import com.abdav.giri_guide.entity.DepositHistory;
import com.abdav.giri_guide.model.response.DepositHistoryListResponse;

public class DepositHistoryMapper {
    private DepositHistoryMapper() {
    }

    public static List<DepositHistoryListResponse> toListOfDepositHistoryListResponse(List<DepositHistory> listData) {
        List<DepositHistoryListResponse> result = new ArrayList<>();
        for (DepositHistory data : listData) {
            result.add(new DepositHistoryListResponse(
                    data.getId(),
                    data.getDeposit().getTourGuide().getId(),
                    data.getDeposit().getTourGuide().getName(),
                    data.getCreatedDate(),
                    data.getNominal(),
                    data.getStatus(),
                    data.getDescription()));
        }

        return result;
    }

}
