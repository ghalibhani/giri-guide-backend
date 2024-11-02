package com.abdav.giri_guide.mapper;

import com.abdav.giri_guide.entity.Transaction;
import com.abdav.giri_guide.model.response.TransactionResponse;

public class TransactionMapper {
    public static TransactionResponse transactionToTransactionResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getStatus().toString(),
                transaction.getSchedule(),
                transaction.getCustomer().getId(),
                transaction.getGuide(),
                transaction.getPorterQty(),
                transaction.getPricePorter(),
                transaction.getTransactionHikers().size(),
                transaction.getTotalPrice()
        );
    }
}
