package com.abdav.giri_guide.mapper;

import com.abdav.giri_guide.entity.Transaction;
import com.abdav.giri_guide.model.response.HikerDetailResponse;
import com.abdav.giri_guide.model.response.TransactionDetailResponse;
import com.abdav.giri_guide.model.response.TransactionResponse;
import com.abdav.giri_guide.util.UrlUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;

import java.time.temporal.ChronoUnit;
import java.util.List;

public class TransactionMapper {
    public static TransactionResponse transactionToTransactionResponse(Transaction transaction, HttpServletRequest httpReq) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getStatus().toString(),
                transaction.getHikingPoint().getMountain().getName(),
                transaction.getHikingPoint().getId(),
                transaction.getHikingPoint().getName(),
                transaction.getStartDate(),
                transaction.getEndDate(),
                getDay(transaction),
                transaction.getCustomer().getId(),
                transaction.getCustomer().getImage() == null ? null : UrlUtil.resolveImageUrl(transaction.getCustomer().getImage(), httpReq),
                transaction.getCustomer().getFullName(),
                transaction.getTourGuide().getId(),
                transaction.getTourGuide().getImage() == null ?null : UrlUtil.resolveImageUrl(transaction.getTourGuide().getImage(), httpReq),
                transaction.getTourGuide().getName(),
                transaction.getPorterQty(),
                transaction.getTransactionHikers().size(),
                transaction.getTotalPrice()
        );
    }

    public static TransactionDetailResponse transactionToTransactionDetailResponse(Transaction transaction, HttpServletRequest httpReq){
        return new TransactionDetailResponse(
                transaction.getId(),
                transaction.getStatus().toString(),
                transaction.getHikingPoint().getMountain().getName(),
                transaction.getHikingPoint().getName(),
                transaction.getStartDate(),
                transaction.getEndDate(),
                getDay(transaction),
                transaction.getCustomer().getId(),
                transaction.getCustomer().getFullName(),
                transaction.getCustomer().getImage() == null ? null : UrlUtil.resolveImageUrl(transaction.getCustomer().getImage(), httpReq),
                transaction.getTourGuide().getId(),
                transaction.getTourGuide().getName(),
                transaction.getTourGuide().getImage() == null ? null : UrlUtil.resolveImageUrl(transaction.getTourGuide().getImage(), httpReq),
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
                transaction.getAdminCost(),
                transaction.getTotalPrice(),
                transaction.getCustomerNote(),
                transaction.getEndOfPayTime(),
                transaction.getEndOfApprove(),
                transaction.getRejectedNote()
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
        Long days = ChronoUnit.DAYS.between(transaction.getStartDate().toLocalDate(), transaction.getEndDate().toLocalDate());
        return days;
    }
}
