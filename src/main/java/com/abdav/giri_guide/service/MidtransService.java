package com.abdav.giri_guide.service;

import com.abdav.giri_guide.entity.Transaction;
import com.abdav.giri_guide.model.response.TransactionStatusResponse;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransSnapApi;

public interface MidtransService {
    TransactionStatusResponse createToken(Transaction transaction) throws MidtransError;
}
