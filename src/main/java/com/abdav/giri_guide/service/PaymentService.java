package com.abdav.giri_guide.service;

import com.abdav.giri_guide.entity.Payment;
import com.abdav.giri_guide.entity.TransactionPayment;
import com.midtrans.httpclient.error.MidtransError;

public interface PaymentService {
    Payment create(TransactionPayment transactionPayment) throws MidtransError;
    void updateStatus(Payment payment, String status);
}
