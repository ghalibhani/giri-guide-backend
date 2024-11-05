package com.abdav.giri_guide.mapper;

import com.abdav.giri_guide.entity.Transaction;
import com.abdav.giri_guide.model.response.HikerDetailResponse;
import com.abdav.giri_guide.model.response.TransactionDetailResponse;
import com.abdav.giri_guide.model.response.TransactionDetailResponseUser;
import com.abdav.giri_guide.model.response.TransactionResponse;
import org.jetbrains.annotations.NotNull;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionMapper {
    public static TransactionResponse transactionToTransactionResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getStatus().toString(),
                transaction.getHikingPoint().getMountain().getName(),
                transaction.getHikingPoint().getId(),
                transaction.getStartDate(),
                transaction.getEndDate(),
                getDay(transaction),
                transaction.getCustomer().getId(),
                transaction.getTourGuide().getName(),
                transaction.getPorterQty(),
                transaction.getTransactionHikers().size(),
                transaction.getTotalPrice()
        );
    }

    public static TransactionDetailResponse transactionToAdminResponse(Transaction transaction){
        return new TransactionDetailResponse(
                transaction.getId(),
                transaction.getStatus().toString(),
                transaction.getHikingPoint().getMountain().getName(),
                transaction.getHikingPoint().getName(),
                getDay(transaction), // -
                transaction.getCustomer().getId(),
                transaction.getCustomer().getFullName(), // -
                transaction.getTourGuide().getId(),
                transaction.getTourGuide().getName(),
                transaction.getTourGuide().getImage().getPath(),
                transaction.getTransactionHikers().size(), // -
                getHikerDetailResponsesList(transaction),
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

    public static TransactionDetailResponseUser transactionToDetailResponseUser(Transaction transaction){
        return new TransactionDetailResponseUser(
                transaction.getId(),
                transaction.getStatus().toString(),
                transaction.getHikingPoint().getMountain().getName(),
                transaction.getHikingPoint().getName(),
                transaction.getStartDate(),
                transaction.getEndDate(),
                getDay(transaction),
                transaction.getCustomer().getId(),
                transaction.getTourGuide().getId(),
                transaction.getTourGuide().getName(),
                getHikerDetailResponsesList(transaction),
                transaction.getPorterQty(),
                transaction.getTourGuide().getPricePorter(),
                transaction.getTotalPorterPrice(),
                transaction.getTourGuide().getPrice(),
                transaction.getTotalTourGuidePrice(),
                transaction.getTourGuide().getAdditionalPrice(),
                transaction.getAdditionalPriceTourGuide(),
                transaction.getHikingPoint().getMountain().getPriceSimaksi(),
                transaction.getTotalSimaksiPrice(),
                transaction.getHikingPoint().getPrice(),
                transaction.getTotalEntryPrice(),
                transaction.getTotalPrice()
        );
    }

    private static @NotNull List<HikerDetailResponse> getHikerDetailResponsesList(Transaction transaction) {
        List<HikerDetailResponse> hikerDetailResponses = transaction.getTransactionHikers().stream().map(hiker -> new HikerDetailResponse(
                hiker.getFullName(),
                hiker.getNik(),
                hiker.getBirthDate()
        )).toList();
        return hikerDetailResponses;
    }

    //start date, end date, tour guide per hari, porter per hari,

    private static @NotNull Long getDay(Transaction transaction) {
        Long days = ChronoUnit.DAYS.between(transaction.getStartDate(), transaction.getEndDate());
        return days;
    }
}
