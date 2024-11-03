package com.abdav.giri_guide.controller;

import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.constant.PathApi;
import com.abdav.giri_guide.model.request.TransactionRequest;
import com.abdav.giri_guide.model.response.CommonResponse;
import com.abdav.giri_guide.model.response.TransactionResponse;
import com.abdav.giri_guide.model.response.TransactionStatusResponse;
import com.abdav.giri_guide.service.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathApi.TRANSACTIONS_API)
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {
    private final TransactionService transactionService;
    private static String message;

    @PostMapping()
    ResponseEntity<?> createTransaction(@RequestBody TransactionRequest transactionRequest) {
        TransactionStatusResponse transactionStatusResponse = transactionService.createTransaction(transactionRequest);
        message = Message.DATA_CREATED;
        CommonResponse<?> response = new CommonResponse<>(message, transactionStatusResponse);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> approveTourGuide(@PathVariable String id){
        TransactionStatusResponse transactionStatusResponse = transactionService.approveTourGuide(id);
        message = Message.DATA_UPDATED;
        CommonResponse<?> response = new CommonResponse<>(message, transactionStatusResponse);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
