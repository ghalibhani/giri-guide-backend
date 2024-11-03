package com.abdav.giri_guide.mapper;

import com.abdav.giri_guide.entity.Transaction;
import com.abdav.giri_guide.model.response.TransactionResponse;

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
}
