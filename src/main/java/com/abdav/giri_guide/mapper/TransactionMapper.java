package com.abdav.giri_guide.mapper;

import com.abdav.giri_guide.entity.Transaction;
import com.abdav.giri_guide.model.response.HikerDetailResponse;
import com.abdav.giri_guide.model.response.TransactionDetailResponse;
import com.abdav.giri_guide.model.response.TransactionResponse;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionMapper {
    public static TransactionResponse transactionToTransactionResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getStatus().toString(),
                transaction.getHikingPoint().getId(),
                transaction.getStartDate(),
                transaction.getEndDate(),
                transaction.getCustomer().getId(),
                transaction.getTourGuide().getId(),
                transaction.getPorterQty(),
                transaction.getTransactionHikers().size(),
                transaction.getTotalPorterPrice(),
                transaction.getTotalTourGuidePrice(),
                transaction.getAdditionalPriceTourGuide(),
                transaction.getTotalSimaksiPrice(),
                transaction.getTotalEntryPrice(),
                transaction.getAdminCost(),
                transaction.getTotalPrice()
        );
    }

    public static TransactionDetailResponse transactionToAdminResponse(Transaction transaction){
        Long days = ChronoUnit.DAYS.between(transaction.getStartDate(), transaction.getEndDate());

        List<HikerDetailResponse> hikerDetailResponses = transaction.getTransactionHikers().stream().map(hiker -> new HikerDetailResponse(
                hiker.getFullName(),
                hiker.getNik(),
                hiker.getBirthDate()
        )).toList();

        return new TransactionDetailResponse(
                transaction.getStatus().toString(),
                transaction.getHikingPoint().getMountain().getName(),
                transaction.getHikingPoint().getName(),
                days,
                transaction.getCustomer().getId(),
                transaction.getCustomer().getFullName(),
                transaction.getTourGuide().getId(),
                transaction.getTourGuide().getName(),
                transaction.getTourGuide().getImage().getPath(),
                transaction.getTransactionHikers().size(),
                hikerDetailResponses,
                transaction.getPorterQty(),
                transaction.getTotalPorterPrice(),
                transaction.getTotalTourGuidePrice(),
                transaction.getAdditionalPriceTourGuide(),
                transaction.getTotalSimaksiPrice(),
                transaction.getTotalEntryPrice(),
                transaction.getAdminCost(),
                transaction.getTotalPrice()
        );
    }
}
