package com.abdav.giri_guide.service;

import com.abdav.giri_guide.entity.TransactionPayment;
import com.abdav.giri_guide.model.response.RevenueResponse;
import com.abdav.giri_guide.model.response.TransactionPaymentResponse;
import com.midtrans.httpclient.error.MidtransError;

import java.util.List;

public interface TransactionPaymentService {
    TransactionPaymentResponse create(String transactionId) throws MidtransError;
    TransactionPayment getById (String orderId);
    void updateStatusPayment(String orderId, String status, String paymentType);
    List<TransactionPayment> transactionPaymentList();
    RevenueResponse getRevenue();
}
