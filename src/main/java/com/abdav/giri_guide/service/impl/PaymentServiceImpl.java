package com.abdav.giri_guide.service.impl;

import com.abdav.giri_guide.entity.Payment;
import com.abdav.giri_guide.entity.TransactionPayment;
import com.abdav.giri_guide.model.request.PaymentDetailRequest;
import com.abdav.giri_guide.model.request.PaymentRequest;
import com.abdav.giri_guide.repository.PaymentRepository;
import com.abdav.giri_guide.service.PaymentService;
import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransSnapApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final Config midtransConfig;
    private final PaymentRepository paymentRepository;

    @Override
    public Payment create(TransactionPayment transactionPayment) throws MidtransError {
        MidtransSnapApi snapApi = new ConfigFactory(midtransConfig).getSnapApi();
        Long amount = (long) Math.round(transactionPayment.getAmount());
//        PaymentDetailRequest paymentDetailRequest = new PaymentDetailRequest(transactionPayment.getId(), amount);
        Map<String, Object> transactionDetail = Map.of(
                "order_id", transactionPayment.getId(),
                "gross_amount", transactionPayment.getAmount()
        );
        List<String> paymentMethod = List.of("shopeePay", "gopay", "indomaret");

        Map<String, Object> params = Map.of(
                "transaction_details", transactionDetail,
                "enable_payments", paymentMethod
        );
        String token = snapApi.createTransactionToken(params);
        String urlRedirect = snapApi.createTransactionRedirectUrl(params);

        Payment payment = Payment.builder()
                .token(token)
                .redirectUrl(urlRedirect)
                .transactionStatus("ordered")
                .build();
        return paymentRepository.saveAndFlush(payment);
    }


}
