package com.abdav.giri_guide.service;

import com.abdav.giri_guide.model.request.TransactionRequest;
import com.abdav.giri_guide.model.response.TransactionResponse;
import com.abdav.giri_guide.model.response.TransactionStatusResponse;

public interface TransactionService {
    TransactionStatusResponse createTransaction(TransactionRequest transactionRequest);
}
