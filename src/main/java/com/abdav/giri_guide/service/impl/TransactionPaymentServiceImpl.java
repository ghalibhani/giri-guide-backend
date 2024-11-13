package com.abdav.giri_guide.service.impl;

import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.entity.Payment;
import com.abdav.giri_guide.entity.Transaction;
import com.abdav.giri_guide.entity.TransactionPayment;
import com.abdav.giri_guide.model.response.*;
import com.abdav.giri_guide.repository.PaymentRepository;
import com.abdav.giri_guide.repository.TransactionPaymentRepository;
import com.abdav.giri_guide.repository.TransactionRepository;
import com.abdav.giri_guide.service.CustomerService;
import com.abdav.giri_guide.service.PaymentService;
import com.abdav.giri_guide.service.TransactionPaymentService;
import com.abdav.giri_guide.service.TransactionService;
import com.midtrans.httpclient.error.MidtransError;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TransactionPaymentServiceImpl implements TransactionPaymentService {
    private final TransactionService transactionService;
    private final TransactionPaymentRepository transactionPaymentRepository;
    private final PaymentService paymentService;
    private final TransactionRepository transactionRepository;
    private final CustomerService customerService;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransactionPaymentResponse create(String transactionId) throws MidtransError {
        Transaction findTransaction = transactionRepository.findById(transactionId).orElseThrow(() -> new EntityNotFoundException("Transaction "+Message.DATA_NOT_FOUND));
        Optional<TransactionPayment> existingTransactionPayment = transactionPaymentRepository.findByTransactionId(transactionId);

        if(existingTransactionPayment.isPresent()){
            TransactionPayment presentTransactionPayment = existingTransactionPayment.get();
            Payment payment = presentTransactionPayment.getPayment();
            PaymentResponse paymentResponse = new PaymentResponse(
                    payment.getId(),
                    payment.getToken(),
                    payment.getRedirectUrl(),
                    payment.getPaymentStatus()
            );
            return new TransactionPaymentResponse(
                presentTransactionPayment.getId(),
                    presentTransactionPayment.getTransaction().getId(),
                    findTransaction.getCustomer().getId(),
                    presentTransactionPayment.getAmount(),
                    presentTransactionPayment.getDate(),
                    paymentResponse
            );
        }

        Long grossAmount = transactionService.getTotalPrice(findTransaction);
        TransactionPayment transactionPayment = TransactionPayment.builder()
                .transaction(findTransaction)
                .amount(grossAmount)
                .build();
        TransactionPayment savedTransactionPayment = transactionPaymentRepository.saveAndFlush(transactionPayment);
        Payment payment = paymentService.create(savedTransactionPayment);
        savedTransactionPayment.setPayment(payment);
        PaymentResponse paymentResponse = new PaymentResponse(
                payment.getId(),
                payment.getToken(),
                payment.getRedirectUrl(),
                payment.getPaymentStatus()
        );
        return new TransactionPaymentResponse(
                savedTransactionPayment.getId(),
                savedTransactionPayment.getTransaction().getId(),
                findTransaction.getCustomer().getId(),
                savedTransactionPayment.getAmount(),
                savedTransactionPayment.getDate(),
                paymentResponse
        );
    }

    @Override
    public TransactionPayment getById(String orderId) {
        TransactionPayment findPayment = transactionPaymentRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Transaction Payment " + Message.DATA_NOT_FOUND));
        return findPayment;
    }

    @Override
    public void updateStatusPayment(String orderId, String status, String paymentType) {
        TransactionPayment transactionPayment =getById(orderId);
        Payment payment = transactionPayment.getPayment();
        paymentService.updateStatus(payment, status, paymentType);

        Transaction transaction = transactionPayment.getTransaction();
        transactionService.updateStatusFromPayment(transaction, status);
        transactionPaymentRepository.saveAndFlush(transactionPayment);
    }

    @Override
    public List<TransactionPayment> transactionPaymentList() {
        return transactionPaymentRepository.findAllByDeletedDateIsNull();
    }

    @Override
    public RevenueResponse getRevenue() {
        List<TransactionPayment> transactionPaymentsList = transactionPaymentList();
        YearMonth currentYearMonth = YearMonth.now();

        Map<YearMonth, Long> revenueOneYearBefore = IntStream.rangeClosed(0, 11)
                .mapToObj(currentYearMonth::minusMonths)
                .collect(Collectors.toMap(month -> month, month -> 0L));
        Map<YearMonth, Long> actualRevenue = transactionPaymentsList.stream()
                .filter(transactionPayment -> transactionPayment.getPayment() != null) // Check for null payment
                .filter(transactionPayment -> "PAID".equals(transactionPayment.getPayment().getPaymentStatus()))
                .filter(transactionPayment -> {
                    YearMonth transactionYearMonth = YearMonth.from(
                            transactionPayment.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    );
                    return !transactionYearMonth.isBefore(currentYearMonth.minusMonths(12));
                })
                .collect(Collectors.groupingBy(
                        transactionPayment -> YearMonth.from(transactionPayment.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()),
                        Collectors.summingLong(transactionPayment -> transactionPayment.getTransaction().getAdminCost()-4000)
                ));

        actualRevenue.forEach(revenueOneYearBefore::put);

        return new RevenueResponse(revenueOneYearBefore);
    }

    @Override
    public DashboardResponse getInfoDashboard(Integer month, Integer year) {
        if(month == null){
            month = LocalDate.now().minusMonths(1).getMonthValue();
        }
        if(year == null){
            year = LocalDate.now().getYear();
        }

        Long totalIncome = getTotalIncome();
        IncomeYearMonthResponse yearMonthIncome = getIncomeYearMonth(month, year);
        RegisterCountResponse yearMonthRegisterCount = customerService.countRegister(month, year);

        return new DashboardResponse(totalIncome, yearMonthIncome, yearMonthRegisterCount);
    }

    @Override
    public Long getTotalIncome() {
        List<TransactionPayment> allTransactionPayment = transactionPaymentRepository.findAllByDeletedDateIsNull();
        Long totalIncome = allTransactionPayment.stream()
                .filter(transactionPayment -> transactionPayment.getPayment() != null && transactionPayment.getPayment().getPaymentStatus().equals("PAID"))
                .mapToLong(transactionPayment -> transactionPayment.getTransaction().getAdminCost() - 4000)
                .sum();
        return totalIncome;
    }

    @Override
    public IncomeYearMonthResponse getIncomeYearMonth(Integer month, Integer year) {
        List<TransactionPayment> allTransactionPayment = transactionPaymentRepository.findAllByDeletedDateIsNull();

        YearMonth findYearMonth = YearMonth.of(year, month);

        Long income = allTransactionPayment.stream()
                .filter(transactionPayment -> transactionPayment.getPayment() != null && transactionPayment.getPayment().getPaymentStatus().equals("PAID"))
                .filter(transactionPayment -> {
                    YearMonth yearMonthIncome = YearMonth.from(transactionPayment.getCreatedDate().toLocalDate());
                    return yearMonthIncome.equals(findYearMonth);
                })
                .mapToLong(transactionPayment -> transactionPayment.getTransaction().getAdminCost() - 4000)
                .sum();

        return new IncomeYearMonthResponse(findYearMonth, income);
    }
}
