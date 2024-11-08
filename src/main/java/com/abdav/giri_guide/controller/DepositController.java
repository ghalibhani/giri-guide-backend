package com.abdav.giri_guide.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abdav.giri_guide.constant.EDepositStatus;
import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.constant.PathApi;
import com.abdav.giri_guide.entity.Deposit;
import com.abdav.giri_guide.model.request.WithdrawRequest;
import com.abdav.giri_guide.model.response.CommonResponse;
import com.abdav.giri_guide.service.DepositService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathApi.TOUR_GUIDE_API)
public class DepositController {
    private final DepositService depositService;

    @GetMapping("/deposit/{userId}")
    public ResponseEntity<?> getUserDepositHistory(
            @PathVariable String userId,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            @RequestParam(required = false, defaultValue = "1") Integer page

    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(depositService.getUserDepositHistories(userId, size, page));
    }

    @PostMapping("/deposit/{userId}/withdraw")
    public ResponseEntity<?> withdrawBalance(
            @PathVariable String userId,
            @RequestBody @Validated WithdrawRequest request

    ) {
        Deposit deposit = depositService.getUserDeposit(userId);
        depositService.withdrawMoney(deposit, request.nominal(), request.message().trim());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(Message.DATA_CREATED, null));
    }

    @GetMapping("/deposit/pending/{tourGuideId}")
    public ResponseEntity<?> getPendingTourGuideDepositHistory(
            @PathVariable String tourGuideId,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            @RequestParam(required = false, defaultValue = "1") Integer page

    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(depositService.getTourGuideAllPendingWithdraw(tourGuideId, size, page));
    }

    @PostMapping("/deposit/add/{tourGuideId}")
    public ResponseEntity<?> sendMoney(
            @PathVariable String tourGuideId,
            @RequestBody WithdrawRequest request

    ) {
        depositService.addMoneyToTourGuide(tourGuideId, request.nominal(), request.message());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(Message.DATA_CREATED, null));
    }

    @PatchMapping("/deposits/{depositHistoryId}")
    public ResponseEntity<?> approveOrRejectWithdraw(
            @PathVariable String depositHistoryId,
            @RequestParam boolean approved,
            @RequestParam(required = false, defaultValue = "Rejected") String message) {

        depositService.approveWithdraw(depositHistoryId, approved, message);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(Message.DATA_CREATED, null));
    }

    @GetMapping("/deposits")
    public ResponseEntity<?> getDepositList(
            @RequestParam(required = false, defaultValue = "") String status,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size

    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(depositService.getDepositList(status, size, page));
    }

}
