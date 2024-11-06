package com.abdav.giri_guide.service;

import com.abdav.giri_guide.model.request.TransactionByStatusRequest;
import com.abdav.giri_guide.entity.Transaction;
import com.abdav.giri_guide.model.request.TransactionRequest;
import com.abdav.giri_guide.model.response.TransactionDetailResponse;
import com.abdav.giri_guide.model.response.TransactionResponse;
import com.abdav.giri_guide.model.response.TransactionStatusResponse;
import com.midtrans.httpclient.error.MidtransError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionService {
    TransactionStatusResponse createTransaction(TransactionRequest transactionRequest);
    TransactionStatusResponse updateTransactionStatus(String id, String status) throws MidtransError;
    Page<TransactionDetailResponse> transactionList(Integer page, Integer size);
    TransactionDetailResponse getTransactionById(String id);
    Page<TransactionResponse> findAllByStatus(List<String> stringList,String userId, Integer page, Integer size, HttpServletRequest httpReq);

    Transaction getById(String id);
}

