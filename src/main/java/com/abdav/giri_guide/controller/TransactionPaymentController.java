package com.abdav.giri_guide.controller;

import com.abdav.giri_guide.constant.PathApi;
import com.abdav.giri_guide.model.response.CommonResponse;
import com.abdav.giri_guide.model.response.CustomerResponse;
import com.abdav.giri_guide.model.response.TransactionPaymentResponse;
import com.abdav.giri_guide.service.TransactionPaymentService;
import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.httpclient.error.MidtransError;
import com.midtrans.service.MidtransCoreApi;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathApi.TRANSACTION_PAYMENT_API)
public class TransactionPaymentController {
    private final TransactionPaymentService transactionPaymentService;
    private final Config midtransConfig;
    private static String message;

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<?> createPayment(
            @RequestParam String transactionId
    ) throws MidtransError {
        TransactionPaymentResponse transactionPaymentResponse = transactionPaymentService.create(transactionId);
        message = "Success create payment";
        CommonResponse<?> response = new CommonResponse<>(message, transactionPaymentResponse);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping(value = "/notification", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> handleNotification(@RequestBody Map<String, Object> response) throws MidtransError{
        MidtransCoreApi coreApi = new ConfigFactory(midtransConfig).getCoreApi();
        String notifResponse = null;
        if(!(response.isEmpty())){
            String orderId = (String) response.get("order_id");
            JSONObject transactionResult = coreApi.checkTransaction(orderId);

            String paymentType = (String) transactionResult.get("payment_type");
            String transactionStatus = (String) transactionResult.get("transaction_status");

            notifResponse = "Transaction notification received. Order ID: " + orderId + ". Transaction status: " + transactionStatus;
            System.out.println(notifResponse);

            if (transactionStatus.equals("settlement")){
                transactionPaymentService.updateStatusPayment(orderId, "PAID", paymentType);
            } else if (transactionStatus.equals("expire")) {
                transactionPaymentService.updateStatusPayment(orderId, "EXPIRED", paymentType);
            } else if (transactionStatus.equals("pending")) {
                transactionPaymentService.updateStatusPayment(orderId, "PENDING", paymentType);
            }
        }
        return ResponseEntity.ok().body(new CommonResponse<>("success", null));
    }
}
