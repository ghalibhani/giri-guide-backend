package com.abdav.giri_guide.service.impl;

import com.abdav.giri_guide.constant.ETransactionStatus;
import com.abdav.giri_guide.entity.Transaction;
import com.abdav.giri_guide.model.response.TransactionStatusResponse;
import com.abdav.giri_guide.service.MidtransService;
import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransSnapApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MidtransServiceImpl implements MidtransService {
    private final Config midtransConfig;
    @Override
    public TransactionStatusResponse createToken(Transaction transaction) throws MidtransError {
        MidtransSnapApi snapApi = new ConfigFactory(midtransConfig).getSnapApi();

        Map<String, Object> requestBody = new HashMap<>();

        Map<String, Object> transactionDetail = new HashMap<>();
        transactionDetail.put("order_id", transaction.getId());
        transactionDetail.put("gross_amount", transaction.getTotalPrice());

        requestBody.put("transaction_details", transactionDetail);

        String transactionToken = snapApi.createTransactionRedirectUrl(requestBody);
        return  new TransactionStatusResponse(ETransactionStatus.WAITING_PAY.toString(), transactionToken);
    }
}
