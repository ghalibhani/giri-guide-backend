package com.abdav.giri_guide.service;

import com.abdav.giri_guide.entity.Transaction;
import com.abdav.giri_guide.model.request.TransactionRequest;
import com.abdav.giri_guide.model.response.TransactionDetailResponse;
import com.abdav.giri_guide.model.response.TransactionStatusResponse;
import com.midtrans.httpclient.error.MidtransError;
import org.springframework.data.domain.Page;

public interface TransactionService {
    TransactionStatusResponse createTransaction(TransactionRequest transactionRequest);
    TransactionStatusResponse updateTransactionStatus(String id, String status) throws MidtransError;
    Page<TransactionDetailResponse> transactionList(Integer page, Integer size);
    TransactionDetailResponse getTransactionById(String id);

    Transaction getById(String id);
}

