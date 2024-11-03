package com.abdav.giri_guide.service;

import com.abdav.giri_guide.model.request.TransactionRequest;
import com.abdav.giri_guide.model.response.TransactionDetailResponse;
import com.abdav.giri_guide.model.response.TransactionStatusResponse;
import org.springframework.data.domain.Page;

public interface TransactionService {
    TransactionStatusResponse createTransaction(TransactionRequest transactionRequest);
    TransactionStatusResponse approveTourGuide(String id);
    Page<TransactionDetailResponse> transactionList(Integer page, Integer size);
}

