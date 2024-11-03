package com.abdav.giri_guide.service;

import com.abdav.giri_guide.model.request.TransactionRequest;
import com.abdav.giri_guide.model.response.TransactionDetailResponse;
import com.abdav.giri_guide.model.response.TransactionStatusResponse;
import org.springframework.data.domain.Page;

public interface TransactionService {
    TransactionStatusResponse createTransaction(TransactionRequest transactionRequest);
    TransactionStatusResponse updateTransactionStatus(String id, String status);
    Page<TransactionDetailResponse> transactionList(Integer page, Integer size);
    TransactionDetailResponse getTransactionById(String id);
}

