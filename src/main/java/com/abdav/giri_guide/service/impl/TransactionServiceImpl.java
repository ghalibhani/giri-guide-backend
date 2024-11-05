package com.abdav.giri_guide.service.impl;

import com.abdav.giri_guide.constant.ETransactionStatus;
import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.model.request.TransactionByStatusRequest;
import com.abdav.giri_guide.entity.*;
import com.abdav.giri_guide.mapper.TransactionMapper;
import com.abdav.giri_guide.model.request.HikerDetailRequest;
import com.abdav.giri_guide.model.request.TransactionRequest;
import com.abdav.giri_guide.model.response.TransactionDetailResponse;
import com.abdav.giri_guide.model.response.TransactionDetailResponseUser;
import com.abdav.giri_guide.model.response.TransactionResponse;
import com.abdav.giri_guide.model.response.TransactionStatusResponse;
import com.abdav.giri_guide.repository.*;
import com.abdav.giri_guide.service.MidtransService;
import com.abdav.giri_guide.service.TransactionService;
import com.midtrans.httpclient.error.MidtransError;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final MountainsRepository mountainsRepository;
    private final CustomerRepository customerRepository;
    private final HikingPointRepository hikingPointRepository;
    private final TransactionHikerRepository transactionHikerRepository;
    private final TourGuideRepository tourGuideRepository;
    private final MidtransService midtransService;

    @Value("${app.giri-guide.admin-cost}")
    private Long adminCost;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransactionStatusResponse createTransaction(TransactionRequest transactionRequest) {
        Customer customer = customerRepository.findById(transactionRequest.customerId()).orElseThrow(EntityNotFoundException::new);
        HikingPoint hikingPointReq = hikingPointRepository.findByIdAndDeletedDateIsNull(transactionRequest.hikingPointId()).orElseThrow(EntityNotFoundException::new);
        Mountains mountain = mountainsRepository.findById(hikingPointReq.getMountain().getId()).orElseThrow(EntityNotFoundException::new);
        TourGuide tourGuide = tourGuideRepository.findById(transactionRequest.guideId()).orElseThrow(() -> new EntityNotFoundException("Tour Guide not found"));

        Transaction transaction = Transaction.builder()
                .customer(customer)
                .tourGuide(tourGuide)
                .hikingPoint(hikingPointReq)
                .startDate(transactionRequest.startDate())
                .endDate(transactionRequest.endDate())
                .status(ETransactionStatus.WAITING_APPROVE)
                .porterQty(transactionRequest.porterQty())
                .adminCost(adminCost)
                .build();
        transactionRepository.saveAndFlush(transaction);

        List<TransactionHiker> hikers = new ArrayList<>();
        for (HikerDetailRequest hiker : transactionRequest.hikerDetails()) {
            TransactionHiker transactionHiker = TransactionHiker.builder()
                    .fullName(hiker.fullName())
                    .nik(hiker.nik())
                    .birthDate(hiker.birthDate())
                    .transaction(transaction)
                    .build();
            hikers.add(transactionHiker);
        }
        transactionHikerRepository.saveAllAndFlush(hikers);
        transaction.setTransactionHikers(hikers);
        Long days = ChronoUnit.DAYS.between(transaction.getStartDate(), transaction.getEndDate());

        Long totalTourguidePrice = tourGuide.getPrice() * days;
        Long totalPorterPrice = calculatePorterPrice(tourGuide.getPricePorter(), transactionRequest.porterQty(), days);
        Long totalAdditionalPrice = calculateAdditionalPrice(tourGuide, hikers.size(), days);
        Long totalSimaksiPrice = calculateSimaksiPrice(mountain, hikers.size());
        Long totalEntryPrice = hikingPointReq.getPrice() * hikers.size() * days;
        Long totalPrice = totalPorterPrice + totalTourguidePrice + totalAdditionalPrice + totalEntryPrice + totalSimaksiPrice + adminCost;


        transaction.setTotalPorterPrice(totalPorterPrice);
        transaction.setTotalTourGuidePrice(totalTourguidePrice);
        transaction.setAdditionalPriceTourGuide(totalAdditionalPrice);
        transaction.setTotalSimaksiPrice(totalSimaksiPrice);
        transaction.setTotalEntryPrice(totalEntryPrice);
        transaction.setTotalPrice(totalPrice);

        transactionRepository.saveAndFlush(transaction);

        return new TransactionStatusResponse(transaction.getStatus().toString(), null);
    }

    @Override
    public TransactionStatusResponse updateTransactionStatus(String id, String status) throws MidtransError {
        Transaction transaction = getTransactionOrThrowNotFound(id);
        ETransactionStatus transactionStatus = ETransactionStatus.valueOf(status.toUpperCase());

        transaction.setStatus(transactionStatus);
        transactionRepository.saveAndFlush(transaction);
//        if (transactionStatus == ETransactionStatus.WAITING_PAY){
//            return midtransService.createToken(transaction);
//        }

        return new TransactionStatusResponse(transaction.getStatus().toString(), null);
    }

    private Transaction getTransactionOrThrowNotFound(String id) {
        return transactionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Message.DATA_NOT_FOUND));
    }

    @Override
    public Page<TransactionDetailResponse> transactionList(Integer page, Integer size) {
        if(page <= 0){
            page = 1;
        }

        Pageable pageable = PageRequest.of(page-1, size);
        Page<Transaction> transactions = transactionRepository.findAllByDeletedDateIsNull(pageable);

        return transactions.map(TransactionMapper::transactionToAdminResponse);
    }

    @Override
    public TransactionDetailResponse getTransactionById(String id) {
        Transaction transaction = getTransactionOrThrowNotFound(id);
        return TransactionMapper.transactionToAdminResponse(transaction);
    }

    @Override
    public Page<TransactionResponse> findAllByStatus(TransactionByStatusRequest request, Integer page, Integer size) {

        List<ETransactionStatus> eStatus = request.listStatus().stream().map(s -> ETransactionStatus.valueOf(s.toUpperCase())).toList();
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Transaction> transactions = transactionRepository.findAllByStatusInAndDeletedDateIsNull(eStatus,pageable);

        return transactions.map(TransactionMapper::transactionToTransactionResponse);
    }

    @Override
    public TransactionDetailResponseUser getByIdTransactionDetailResponseUser(String id) {
        Transaction transaction = getTransactionOrThrowNotFound(id);

        return TransactionMapper.transactionToDetailResponseUser(transaction);
    }

    @Override
    public Transaction getById(String id) {
        return getTransactionOrThrowNotFound(id);
    }


    private Long calculatePorterPrice(Long porterRate, Integer porterQty, Long days){
        return porterRate * porterQty * days;
    }

    private Long calculateSimaksiPrice(Mountains mountains, int hikerQty){
        return mountains.isUseSimaksi() ? mountains.getPriceSimaksi() * hikerQty : 0L;
    }

    private Long calculateAdditionalPrice(TourGuide tourGuide, int hikerQty, Long days){
        if(hikerQty > tourGuide.getMaxHiker()){
            int additionalHiker = hikerQty - tourGuide.getMaxHiker();
            return tourGuide.getAdditionalPrice() * additionalHiker * days;
        }
        return 0L;
    }
}
