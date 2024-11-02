package com.abdav.giri_guide.service.impl;

import com.abdav.giri_guide.constant.ETransactionStatus;
import com.abdav.giri_guide.entity.*;
import com.abdav.giri_guide.mapper.TransactionMapper;
import com.abdav.giri_guide.model.request.HikerDetailRequest;
import com.abdav.giri_guide.model.request.TransactionRequest;
import com.abdav.giri_guide.model.response.HikingPointResponse;
import com.abdav.giri_guide.model.response.MountainsDetailResponse;
import com.abdav.giri_guide.model.response.TransactionResponse;
import com.abdav.giri_guide.repository.*;
import com.abdav.giri_guide.service.CustomerService;
import com.abdav.giri_guide.service.MountainsService;
import com.abdav.giri_guide.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final MountainsRepository mountainsRepository;
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final HikingPointRepository hikingPointRepository;
    private final TransactionHikerRepository transactionHikerRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransactionResponse createTransaction(TransactionRequest transactionRequest) {
//        Customer customer = customerService.getCustomerByIdOrThrowNotFound(transactionRequest.customerId());
        Customer customer = customerRepository.findById(transactionRequest.customerId()).orElseThrow(EntityNotFoundException::new);
        HikingPoint hikingPointReq = hikingPointRepository.findByIdAndDeletedDateIsNull(transactionRequest.hikingPointId()).orElseThrow(EntityNotFoundException::new);
        Mountains mountain = mountainsRepository.findById(hikingPointReq.getMountain().getId()).orElseThrow(EntityNotFoundException::new);

        Transaction transaction = Transaction.builder()
                .customer(customer)
                .guide(transactionRequest.guideId())
                .schedule(transactionRequest.schedule())
                .status(ETransactionStatus.WAITING_APPROVE)
                .porterQty(transactionRequest.porterQty())
                .entryPrice(hikingPointReq.getPrice())
                .hikingPoint(hikingPointReq)
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

        //Hard Code Price porter, additional dan tourguide
        Double totalPricePorter = 50000.0 * transactionRequest.porterQty();
        Double priceTourGuide = 200000.0;
        Double additionalPrice = 30000.0 * (hikers.size() - 5);
        Double totalSimaksiPrice = mountain.getPriceSimaksi() * hikers.size();
        Double totalEntryPrice = hikingPointReq.getPrice() *hikers.size();

        transaction.setPricePorter(totalPricePorter);
        transaction.setPriceTourGuide(priceTourGuide);
        transaction.setAdditionalPriceTourGuide(hikers.size() == 5 ? 0.0 : hikers.size() * additionalPrice);
        transaction.setSimaksiPrice(totalSimaksiPrice);
        transaction.setEntryPrice(totalEntryPrice);

        transactionRepository.saveAndFlush(transaction);

        return TransactionMapper.transactionToTransactionResponse(transaction);
    }
}
