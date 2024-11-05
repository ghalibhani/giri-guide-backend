package com.abdav.giri_guide.controller;

import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.constant.PathApi;
import com.abdav.giri_guide.model.request.TransactionByStatusRequest;
import com.abdav.giri_guide.model.request.TransactionRequest;
import com.abdav.giri_guide.model.response.*;
import com.abdav.giri_guide.service.TransactionService;
import com.midtrans.httpclient.error.MidtransError;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    ResponseEntity<?> updateTransactionStatus(@PathVariable String id, @RequestParam String status) throws MidtransError {
        TransactionStatusResponse transactionStatusResponse = transactionService.updateTransactionStatus(id, status);
        message = Message.DATA_UPDATED;
        CommonResponse<?> response = new CommonResponse<>(message, transactionStatusResponse);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping
    ResponseEntity<?> transactionResponseCustomer(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size
    ){
        Page<TransactionDetailResponse> transactionList = transactionService.transactionList(page, size);
        PagingResponse paging = new PagingResponse(page, size, transactionList.getTotalPages(), transactionList.getTotalElements());
        message = Message.SUCCESS_FETCH;
        CommonResponseWithPage<?> response = new CommonResponseWithPage<>(message, transactionList.getContent(), paging);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getTransactionById(@PathVariable String id){
        TransactionDetailResponse transactionDetailResponse = transactionService.getTransactionById(id);
        message = Message.SUCCESS_FETCH;
        CommonResponse<?> response = new CommonResponse<>(message, transactionDetailResponse);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping(PathApi.HISTORY_TRANSACTION)
    ResponseEntity<?> transactionByStatus(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            @RequestBody TransactionByStatusRequest request
    ){
        Page<TransactionResponse> transaction = transactionService.findAllByStatus(request, page, size);
        PagingResponse paging = new PagingResponse(page, size, transaction.getTotalPages(), transaction.getTotalElements());
        message = Message.SUCCESS_FETCH;
        CommonResponseWithPage<?> response = new CommonResponseWithPage<>(message, transaction.getContent(), paging);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
