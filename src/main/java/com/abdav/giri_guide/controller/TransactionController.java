package com.abdav.giri_guide.controller;

import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.constant.PathApi;
import com.abdav.giri_guide.model.request.TransactionRequest;
import com.abdav.giri_guide.model.request.TransactionStatusUpdateRequest;
import com.abdav.giri_guide.model.response.*;
import com.abdav.giri_guide.service.TransactionPaymentService;
import com.abdav.giri_guide.service.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathApi.TRANSACTIONS_API)
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionPaymentService transactionPaymentService;
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
    ResponseEntity<?> updateTransactionStatus(@PathVariable String id, @RequestBody TransactionStatusUpdateRequest request)  {
        TransactionStatusResponse transactionStatusResponse = transactionService.updateTransactionStatus(id, request.status(), request.rejectedNote());
        message = Message.DATA_UPDATED;
        CommonResponse<?> response = new CommonResponse<>(message, transactionStatusResponse);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping
    ResponseEntity<?> transactionResponseCustomer(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            @RequestParam(required = false) String status,
            HttpServletRequest httpReq
    ){
        Page<TransactionDetailResponse> transactionList = transactionService.transactionList(page, size, status, httpReq);
        PagingResponse paging = new PagingResponse(page, size, transactionList.getTotalPages(), transactionList.getTotalElements());
        message = Message.SUCCESS_FETCH;
        CommonResponseWithPage<?> response = new CommonResponseWithPage<>(message, transactionList.getContent(), paging);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getTransactionById(@PathVariable String id, HttpServletRequest httpReq){
        TransactionDetailResponse transactionDetailResponse = transactionService.getTransactionById(id, httpReq);
        message = Message.SUCCESS_FETCH;
        CommonResponse<?> response = new CommonResponse<>(message, transactionDetailResponse);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(PathApi.HISTORY_TRANSACTION)
    ResponseEntity<?> transactionByStatus(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            @RequestParam String userId,
            @RequestParam String status,
            @RequestParam String role,
            HttpServletRequest httpReq
    ){
        List<String> statusList = Arrays.stream(status.split(",")).map(String::toUpperCase).toList();
        Page<TransactionResponse> transaction = transactionService.findAllByStatus(statusList, userId, page, size, role, httpReq);
        PagingResponse paging = new PagingResponse(page, size, transaction.getTotalPages(), transaction.getTotalElements());
        message = Message.SUCCESS_FETCH;
        CommonResponseWithPage<?> response = new CommonResponseWithPage<>(message, transaction.getContent(), paging);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/dashboard/info-status")
    ResponseEntity<?> dashboardAdmin(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ){
        CountTransactionResponse dashboard = transactionService.countAllStatusTransaction(month, year);
        message = Message.SUCCESS_FETCH;
        CommonResponse<?> response = new CommonResponse<>(message, dashboard);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/revenue")
    ResponseEntity<?> getRevenueOneYearBefore(){
        RevenueResponse revenueResponse = transactionPaymentService.getRevenue();
        message = "Revenue" + Message.SUCCESS_FETCH;
        CommonResponse<?> response = new CommonResponse<>(message, revenueResponse);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/dashboard")
    ResponseEntity<?> getInfoDashboard(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ){
        DashboardResponse dashboardResponse = transactionPaymentService.getInfoDashboard(month, year);
        message = Message.SUCCESS_FETCH;
        CommonResponse<?> response = new CommonResponse<>(message, dashboardResponse);

        return ResponseEntity.ok(response);
    }
}
