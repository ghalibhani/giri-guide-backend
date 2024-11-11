package com.abdav.giri_guide.service.impl;

import com.abdav.giri_guide.entity.Payment;
import com.abdav.giri_guide.entity.TransactionPayment;
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
        Map<String, Object> transactionDetail = Map.of(
                "order_id", transactionPayment.getId(),
                "gross_amount", transactionPayment.getAmount()
        );
        List<String> paymentMethod = List.of("shopeePay", "gopay", "indomaret");

        List<Map<String, Object>> itemDetails = List.of(
                Map.of(
                        "id", "ITEM1",
                        "price", transactionPayment.getTransaction().getTotalTourGuidePrice(),
                        "quantity", 1,
                        "name", "Total jasa tour guide"
                ),
                Map.of(
                        "id", "ITEM2",
                        "price", transactionPayment.getTransaction().getTotalEntryPrice(),
                        "quantity", 1,
                        "name", "Total uang masuk gunung"
                ),
                Map.of(
                        "id", "ITEM3",
                        "price", transactionPayment.getTransaction().getTotalSimaksiPrice(),
                        "quantity", 1,
                        "name", "Total simaksi"
                ),
                Map.of(
                        "id", "ITEM4",
                        "price", transactionPayment.getTransaction().getAdditionalPriceTourGuide(),
                        "quantity", 1,
                        "name", "Total tambah orang"
                ),
                Map.of(
                        "id", "ITEM5",
                        "price", transactionPayment.getTransaction().getTotalPorterPrice(),
                        "quantity", 1,
                        "name", "Total porter"
                ),
                Map.of(
                        "id", "ITEM6",
                        "price", transactionPayment.getTransaction().getAdminCost(),
                        "quantity", 1,
                        "name", "Lain-lain"
                )
        );

        Map<String, Object> params = Map.of(
                "transaction_details", transactionDetail,
                "enable_payments", paymentMethod,
                "item_details", itemDetails
        );
        String token = snapApi.createTransactionToken(params);
        String urlRedirect = snapApi.createTransactionRedirectUrl(params);

        Payment payment = Payment.builder()
                .token(token)
                .redirectUrl(urlRedirect)
                .paymentStatus("PENDING")
                .build();
        return paymentRepository.saveAndFlush(payment);
    }

    @Override
    public void updateStatus(Payment payment, String status) {
        payment.setPaymentStatus(status);
        paymentRepository.saveAndFlush(payment);
    }


}
