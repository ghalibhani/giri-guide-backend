package com.abdav.giri_guide.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.abdav.giri_guide.constant.EDepositStatus;
import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.entity.Deposit;
import com.abdav.giri_guide.entity.DepositHistory;
import com.abdav.giri_guide.entity.TourGuide;
import com.abdav.giri_guide.entity.User;
import com.abdav.giri_guide.mapper.DepositHistoryMapper;
import com.abdav.giri_guide.model.response.CommonResponseWithPage;
import com.abdav.giri_guide.model.response.DepositHistoryListResponse;
import com.abdav.giri_guide.model.response.PagingResponse;
import com.abdav.giri_guide.repository.DepositHistoryRepository;
import com.abdav.giri_guide.repository.DepositRepository;
import com.abdav.giri_guide.repository.TourGuideRepository;
import com.abdav.giri_guide.repository.UserRepository;
import com.abdav.giri_guide.service.DepositService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepositServiceImpl implements DepositService {
    private final DepositRepository depositRepository;
    private final DepositHistoryRepository historyRepository;

    private final UserRepository userRepository;
    private final TourGuideRepository tourGuideRepository;

    @Override
    public Deposit getUserDeposit(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User " + Message.DATA_NOT_FOUND));

        TourGuide tourGuide = tourGuideRepository.findByUsersAndDeletedDateIsNull(user)
                .orElseThrow(() -> new EntityNotFoundException("Tour Guide " + Message.DATA_NOT_FOUND));

        if (tourGuide.getDeposit() == null) {
            tourGuide.setDeposit(createDeposit(tourGuide));

        }

        return tourGuide.getDeposit();
    }

    @Override
    public Deposit createDeposit(TourGuide tourGuide) {
        Optional<Deposit> tourGuideDeposit = depositRepository.findByTourGuideAndDeletedDateIsNull(tourGuide);
        if (tourGuideDeposit.isPresent()) {
            throw new DataIntegrityViolationException("tour guide already have deposit");
        }

        Deposit deposit = Deposit.builder()
                .tourGuide(tourGuide)
                .build();

        return depositRepository.save(deposit);
    }

    @Override
    @Transactional
    public DepositHistory addMoney(Deposit deposit, Long nominal, String description) {
        deposit.setMoney(nominal + deposit.getMoney());
        depositRepository.save(deposit);

        DepositHistory history = DepositHistory.builder()
                .deposit(deposit)
                .nominal(nominal)
                .status(EDepositStatus.IN)
                .description(description)
                .build();

        return historyRepository.save(history);
    }

    @Override
    public DepositHistory withdrawMoney(Deposit deposit, Long nominal, String description) {
        if (nominal > deposit.getMoney()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nominal larger than deposit money");
        }
        DepositHistory history = DepositHistory.builder()
                .deposit(deposit)
                .nominal(nominal)
                .status(EDepositStatus.PENDING)
                .description(description)
                .build();

        return historyRepository.save(history);
    }

    @Transactional
    @Override
    public DepositHistory approveWithdraw(String depositHistoryId, boolean isApproved, String description) {
        DepositHistory request = historyRepository.findById(depositHistoryId)
                .orElseThrow(() -> new EntityNotFoundException("Deposit History" + Message.DATA_NOT_FOUND));

        if (request.getStatus() != EDepositStatus.PENDING) {
            throw new DataIntegrityViolationException("not a pending withdraw");
        }

        if (isApproved) {
            request.getDeposit().setMoney(request.getDeposit().getMoney() - request.getNominal());
            depositRepository.save(request.getDeposit());

            request.setStatus(EDepositStatus.OUT);

        } else {
            request.setStatus(EDepositStatus.REJECTED);
            request.setDescription(description);
        }

        return historyRepository.save(request);
    }

    @Override
    public CommonResponseWithPage<List<DepositHistoryListResponse>> getTourGuideAllPendingWithdraw(
            String tourGuideId, Integer size, Integer page) {

        if (page <= 0) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        TourGuide tourGuide = tourGuideRepository.findByIdAndDeletedDateIsNull(tourGuideId)
                .orElseThrow(() -> new EntityNotFoundException("Tour Guide " + Message.DATA_NOT_FOUND));

        if (tourGuide.getDeposit() == null) {
            tourGuide.setDeposit(createDeposit(tourGuide));

        }

        Page<DepositHistory> historyPage = historyRepository
                .findByDepositAndStatusInOrderByCreatedDateDesc(
                        tourGuide.getDeposit(),
                        new ArrayList<>(List.of(EDepositStatus.PENDING)),
                        pageable);

        PagingResponse paging = new PagingResponse(
                page,
                size,
                historyPage.getTotalPages(),
                historyPage.getTotalElements());

        return new CommonResponseWithPage<>(
                Message.SUCCESS_FETCH,
                DepositHistoryMapper.toListOfDepositHistoryListResponse(historyPage.getContent()),
                paging);

    }

    @Override
    public CommonResponseWithPage<List<DepositHistoryListResponse>> getUserDepositHistories(
            String userId,
            Integer size,
            Integer page) {

        Deposit deposit = getUserDeposit(userId);

        if (page <= 0) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<DepositHistory> historyPage = historyRepository
                .findByDepositAndStatusInOrderByCreatedDateDesc(
                        deposit,
                        new ArrayList<>(List.of(EDepositStatus.IN, EDepositStatus.OUT, EDepositStatus.REJECTED)),
                        pageable);

        PagingResponse paging = new PagingResponse(
                page,
                size,
                historyPage.getTotalPages(),
                historyPage.getTotalElements());

        return new CommonResponseWithPage<>(
                Message.SUCCESS_FETCH,
                DepositHistoryMapper.toListOfDepositHistoryListResponse(historyPage.getContent()),
                paging);
    }

    @Override
    public void addMoneyToTourGuide(String tourGuideId, Long nominal, String description) {

        TourGuide tourGuide = tourGuideRepository.findByIdAndDeletedDateIsNull(tourGuideId)
                .orElseThrow(() -> new EntityNotFoundException("Tour Guide " + Message.DATA_NOT_FOUND));

        if (tourGuide.getDeposit() == null) {
            tourGuide.setDeposit(createDeposit(tourGuide));

        }

        addMoney(tourGuide.getDeposit(), nominal, description);
    }

    @Override
    public CommonResponseWithPage<List<DepositHistoryListResponse>> getDepositList(
            String status, String name, Integer size, Integer page) {

        if (page <= 0) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, size);

        List<EDepositStatus> statusList;
        switch (status.toUpperCase().trim()) {
            case "IN":
                statusList = new ArrayList<>(List.of(EDepositStatus.IN));
                break;

            case "OUT":
                statusList = new ArrayList<>(List.of(EDepositStatus.OUT));
                break;

            case "PENDING":
                statusList = new ArrayList<>(List.of(EDepositStatus.PENDING));
                break;

            case "REJECTED":
                statusList = new ArrayList<>(List.of(EDepositStatus.REJECTED));
                break;

            default:
                statusList = new ArrayList<>(List.of(
                        EDepositStatus.IN,
                        EDepositStatus.OUT,
                        EDepositStatus.PENDING,
                        EDepositStatus.REJECTED));
                break;
        }

        Page<DepositHistory> historyPage = historyRepository
                .findByStatusInAndDepositTourGuideNameContainsIgnoreCaseOrderByCreatedDateDesc(
                        statusList, name, pageable);

        PagingResponse paging = new PagingResponse(
                page,
                size,
                historyPage.getTotalPages(),
                historyPage.getTotalElements());

        return new CommonResponseWithPage<>(
                Message.SUCCESS_FETCH,
                DepositHistoryMapper.toListOfDepositHistoryListResponse(historyPage.getContent()),
                paging);

    }

}
