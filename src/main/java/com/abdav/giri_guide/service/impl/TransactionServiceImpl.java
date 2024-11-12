package com.abdav.giri_guide.service.impl;

import com.abdav.giri_guide.constant.ERole;
import com.abdav.giri_guide.constant.ETransactionStatus;
import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.entity.*;
import com.abdav.giri_guide.mapper.TransactionMapper;
import com.abdav.giri_guide.model.request.HikerDetailRequest;
import com.abdav.giri_guide.model.request.TransactionRequest;
import com.abdav.giri_guide.model.response.CountTransactionResponse;
import com.abdav.giri_guide.model.response.TransactionDetailResponse;
import com.abdav.giri_guide.model.response.TransactionResponse;
import com.abdav.giri_guide.model.response.TransactionStatusResponse;
import com.abdav.giri_guide.repository.*;
import com.abdav.giri_guide.service.CustomerService;
import com.abdav.giri_guide.service.GuideReviewService;
import com.abdav.giri_guide.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final MountainsRepository mountainsRepository;
    private final CustomerRepository customerRepository;
    private final HikingPointRepository hikingPointRepository;
    private final TransactionHikerRepository transactionHikerRepository;
    private final TourGuideRepository tourGuideRepository;
    private final CustomerService customerService;
    private final GuideReviewService reviewService;
    private final DepositServiceImpl depositService;

    @Value("${app.giri-guide.admin-cost}")
    private Long adminCost;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransactionStatusResponse createTransaction(TransactionRequest transactionRequest) {
        Customer customer = customerRepository.findById(transactionRequest.customerId())
                .orElseThrow(EntityNotFoundException::new);
        HikingPoint hikingPointReq = hikingPointRepository
                .findByIdAndDeletedDateIsNull(transactionRequest.hikingPointId())
                .orElseThrow(EntityNotFoundException::new);
        Mountains mountain = mountainsRepository.findById(hikingPointReq.getMountain().getId())
                .orElseThrow(EntityNotFoundException::new);
        TourGuide tourGuide = tourGuideRepository.findById(transactionRequest.guideId())
                .orElseThrow(() -> new EntityNotFoundException("Tour Guide not found"));

        Transaction transaction = Transaction.builder()
                .customer(customer)
                .tourGuide(tourGuide)
                .hikingPoint(hikingPointReq)
                .startDate(transactionRequest.startDate())
                .endDate(transactionRequest.endDate())
                .status(ETransactionStatus.WAITING_APPROVE)
                .porterQty(transactionRequest.porterQty())
                .adminCost(adminCost)
                .customerNote(transactionRequest.customerNote())
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
        Long days = ChronoUnit.DAYS.between(transaction.getStartDate().toLocalDate(), transaction.getEndDate().toLocalDate());

        Long totalTourguidePrice = tourGuide.getPrice() * days;
        Long totalPorterPrice = calculatePorterPrice(tourGuide.getPricePorter(), transactionRequest.porterQty(), days);
        Long totalAdditionalPrice = calculateAdditionalPrice(tourGuide, hikers.size(), days);
        Long totalSimaksiPrice = calculateSimaksiPrice(mountain, hikers.size());
        Long totalEntryPrice = hikingPointReq.getPrice() * hikers.size() * days;

        transaction.setTotalPorterPrice(totalPorterPrice);
        transaction.setTotalTourGuidePrice(totalTourguidePrice);
        transaction.setAdditionalPriceTourGuide(totalAdditionalPrice);
        transaction.setTotalSimaksiPrice(totalSimaksiPrice);
        transaction.setTotalEntryPrice(totalEntryPrice);
        transaction.setEndOfApprove(LocalDateTime.now().plusDays(1));

        transactionRepository.saveAndFlush(transaction);

        return new TransactionStatusResponse(transaction.getStatus().toString(), null);
    }

    @Override
    public TransactionStatusResponse updateTransactionStatus(String id, String status, String rejectedNote) {
        Transaction transaction = getTransactionOrThrowNotFound(id);
        ETransactionStatus transactionStatus = ETransactionStatus.valueOf(status.toUpperCase());

        transaction.setStatus(transactionStatus);
        transactionRepository.saveAndFlush(transaction);

        if (transactionStatus == ETransactionStatus.REJECTED && rejectedNote != null) {
            transaction.setRejectedNote(rejectedNote);
            transactionRepository.saveAndFlush(transaction);
            return new TransactionStatusResponse(transaction.getStatus().toString(), transaction.getRejectedNote());
        }
        if (transactionStatus == ETransactionStatus.WAITING_PAY) {
            transaction.setEndOfPayTime(transaction.getLastModifiedDate().plusDays(1));
            transactionRepository.saveAndFlush(transaction);
        }
        if (transactionStatus.equals(ETransactionStatus.DONE)) {
            reviewService.createBlankReview(transaction);
            Long nominal = transaction.getTotalPorterPrice() + transaction.getTotalTourGuidePrice() + transaction.getAdditionalPriceTourGuide();
            depositService.addMoney(
                    transaction.getTourGuide().getDeposit(),
                    nominal,
                    "Uang pelunasan pendakian " + transaction.getHikingPoint().getMountain().getName());
        }

        return new TransactionStatusResponse(transaction.getStatus().toString(), null);
    }

    private Transaction getTransactionOrThrowNotFound(String id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Message.DATA_NOT_FOUND));
    }

    @Override
    public Page<TransactionDetailResponse> transactionList(Integer page, Integer size, String status,
            HttpServletRequest httpReq) {
        if (page <= 0) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Transaction> transactionPage;
        if (status == null) {
            transactionPage = transactionRepository.findAll(pageable);
            return transactionPage
                    .map(transaction -> TransactionMapper.transactionToTransactionDetailResponse(transaction, getTotalPrice(transaction), httpReq));
        } else {

            ETransactionStatus eStatus = ETransactionStatus.valueOf(status.toUpperCase());
            transactionPage = transactionRepository.findAllByStatus(eStatus, pageable);
            return transactionPage
                    .map(transaction -> TransactionMapper.transactionToTransactionDetailResponse(transaction, getTotalPrice(transaction), httpReq));
        }
    }

    @Override
    public TransactionDetailResponse getTransactionById(String id, HttpServletRequest httpReq) {
        Transaction transaction = getTransactionOrThrowNotFound(id);
        return TransactionMapper.transactionToTransactionDetailResponse(transaction, getTotalPrice(transaction), httpReq);
    }

    @Override
    public Page<TransactionResponse> findAllByStatus(List<String> statusList, String userId, Integer page, Integer size,
            String role, HttpServletRequest httpReq) {

        ERole eRole = ERole.valueOf(role.toUpperCase());
        List<ETransactionStatus> eStatus = statusList.stream().map(ETransactionStatus::valueOf).toList();
        Pageable pageable = PageRequest.of(page - 1, size);
        List<Transaction> transactions;
        Page<Transaction> transactionPage;

        if (eRole.equals(ERole.ROLE_CUSTOMER)) {
            Customer customer = customerService.getById(userId);
            transactions = transactionRepository
                    .findAllByCustomerIdAndStatusInAndDeletedDateIsNullOrderByStartDateAsc(customer.getId(), eStatus);
            getUpdateStatusInHistory(transactions);
            transactionPage = transactionRepository
                    .findAllByCustomerIdAndStatusInAndDeletedDateIsNullOrderByStartDateAsc(customer.getId(), eStatus,
                            pageable);
            return transactionPage
                    .map(transaction -> TransactionMapper.transactionToTransactionResponse(transaction, getTotalPrice(transaction), httpReq));
        } else if (eRole.equals(ERole.ROLE_GUIDE)) {
            TourGuide tourGuide = tourGuideRepository.findByUsersIdAndDeletedDateIsNull(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Tour Guide " + Message.DATA_NOT_FOUND));
            transactions = transactionRepository
                    .findAllByTourGuideIdAndStatusInAndDeletedDateIsNullOrderByStartDateAsc(tourGuide.getId(), eStatus);
            getUpdateStatusInHistory(transactions);
            transactionPage = transactionRepository
                    .findAllByTourGuideIdAndStatusInAndDeletedDateIsNullOrderByStartDateAsc(tourGuide.getId(), eStatus,
                            pageable);
            return transactionPage
                    .map(transaction -> TransactionMapper.transactionToTransactionResponse(transaction, getTotalPrice(transaction), httpReq));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public CountTransactionResponse countAllStatusTransaction(Integer month, Integer year) {
        List<Transaction> allTransaction = transactionRepository.findAllByDeletedDateIsNull();
        if(month == null || year == null){
            month = LocalDate.now().minusMonths(1).getMonthValue();
            year = LocalDate.now().getYear();
        }

        Integer finalYear = year;
        Integer finalMonth = month;

        Map<ETransactionStatus, Long> countTransactionGroupByStatus = allTransaction.stream()
                .filter(transaction -> transaction.getCreatedDate().getYear() == finalYear && transaction.getCreatedDate().getMonthValue() == finalMonth)
                .collect(Collectors.groupingBy(Transaction::getStatus, Collectors.counting()));

        for(ETransactionStatus status : ETransactionStatus.values()){
            countTransactionGroupByStatus.putIfAbsent(status, 0L);
        }

        return new CountTransactionResponse(
                countTransactionGroupByStatus,
                finalYear,
                finalMonth
        );
    }

    private void getUpdateStatusInHistory(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            if (transaction.getStatus() == ETransactionStatus.WAITING_PAY && transaction.getEndOfPayTime() != null
                    && transaction.getEndOfPayTime().isBefore(LocalDateTime.now())) {
                transaction.setStatus(ETransactionStatus.REJECTED);
                transactionRepository.saveAndFlush(transaction);
            } else if (transaction.getStatus() == ETransactionStatus.WAITING_APPROVE
                    && transaction.getEndOfApprove() != null
                    && transaction.getEndOfApprove().isBefore(LocalDateTime.now())) {
                transaction.setStatus(ETransactionStatus.REJECTED);
                transactionRepository.saveAndFlush(transaction);
            }
        }
    }

    @Override
    public Transaction getById(String id) {
        return getTransactionOrThrowNotFound(id);
    }

    @Override
    public void updateStatusFromPayment(Transaction transaction, String status) {
        if (status.equals("PAID")) {
            transaction.setStatus(ETransactionStatus.UPCOMING);
            Long nominal = transaction.getTotalSimaksiPrice() + transaction.getTotalEntryPrice();
            depositService.addMoney(
                    transaction.getTourGuide().getDeposit(),
                    nominal,
                    "Uang admin pendakian " + transaction.getHikingPoint().getMountain().getName());

        } else if (status.equals("EXPIRED")) {
            transaction.setStatus(ETransactionStatus.REJECTED);
        }
        transactionRepository.saveAndFlush(transaction);
    }

    private Long calculatePorterPrice(Long porterRate, Integer porterQty, Long days) {
        return porterRate * porterQty * days;
    }

    private Long calculateSimaksiPrice(Mountains mountains, int hikerQty) {
        return mountains.isUseSimaksi() ? mountains.getPriceSimaksi() * hikerQty : 0L;
    }

    private Long calculateAdditionalPrice(TourGuide tourGuide, int hikerQty, Long days) {
        if (hikerQty > tourGuide.getMaxHiker()) {
            throw new DataIntegrityViolationException("Pendaki melebihi kapasitas");
        } else if (hikerQty > 5) {
            int additionalHiker = hikerQty - 5;
            return tourGuide.getAdditionalPrice() * additionalHiker * days;
        }
        return 0L;
    }
    @Override
    public Long getTotalPrice(Transaction transaction){
        return transaction.getTotalPorterPrice() + transaction.getTotalTourGuidePrice() + transaction.getAdditionalPriceTourGuide() +
                transaction.getTotalEntryPrice() + transaction.getTotalSimaksiPrice() +transaction.getAdminCost();
    }
}
