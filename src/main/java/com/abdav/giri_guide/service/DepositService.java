package com.abdav.giri_guide.service;

import java.util.List;

import com.abdav.giri_guide.constant.EDepositStatus;
import com.abdav.giri_guide.entity.Deposit;
import com.abdav.giri_guide.entity.DepositHistory;
import com.abdav.giri_guide.entity.TourGuide;
import com.abdav.giri_guide.model.response.CommonResponseWithPage;
import com.abdav.giri_guide.model.response.DepositHistoryListResponse;

public interface DepositService {

    CommonResponseWithPage<List<DepositHistoryListResponse>> getDepositList(
            String status, Integer size, Integer page);

    Deposit getUserDeposit(String userId);

    Deposit createDeposit(TourGuide tourGuide);

    DepositHistory addMoney(Deposit deposit, Long nominal, String description);

    DepositHistory withdrawMoney(Deposit deposit, Long nominal, String description);

    DepositHistory approveWithdraw(String depositHistoryId, boolean isApproved, String description);

    CommonResponseWithPage<List<DepositHistoryListResponse>> getUserDepositHistories(
            String tourGuideId, Integer size, Integer page);

    CommonResponseWithPage<List<DepositHistoryListResponse>> getTourGuideAllPendingWithdraw(String tourGuideId,
            Integer size, Integer page);

    void addMoneyToTourGuide(String tourGuideId, Long nominal, String description);

}
