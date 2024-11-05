package com.abdav.giri_guide.controller;

import com.abdav.giri_guide.constant.PathApi;
import com.abdav.giri_guide.model.response.CommonResponse;
import com.abdav.giri_guide.model.response.TransactionPaymentResponse;
import com.abdav.giri_guide.service.TransactionPaymentService;
import com.midtrans.httpclient.error.MidtransError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathApi.TRANSACTIONS_API)
public class TransactionPaymentController {
    private final TransactionPaymentService transactionPaymentService;
    private static String message;

    @PostMapping(PathApi.PAYMENTS_API)
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
}
