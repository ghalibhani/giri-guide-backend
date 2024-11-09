package com.abdav.giri_guide.service.impl;

import com.abdav.giri_guide.constant.ETransactionStatus;
import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.entity.Payment;
import com.abdav.giri_guide.entity.Transaction;
import com.abdav.giri_guide.entity.TransactionPayment;
import com.abdav.giri_guide.model.response.PaymentResponse;
import com.abdav.giri_guide.model.response.TransactionPaymentResponse;
import com.abdav.giri_guide.repository.PaymentRepository;
import com.abdav.giri_guide.repository.TransactionPaymentRepository;
import com.abdav.giri_guide.repository.TransactionRepository;
import com.abdav.giri_guide.service.PaymentService;
import com.abdav.giri_guide.service.TransactionPaymentService;
import com.abdav.giri_guide.service.TransactionService;
import com.midtrans.httpclient.error.MidtransError;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionPaymentServiceImpl implements TransactionPaymentService {
    private final TransactionService transactionService;
    private final TransactionPaymentRepository transactionPaymentRepository;
    private final PaymentService paymentService;
    private final TransactionRepository transactionRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransactionPaymentResponse create(String transactionId) throws MidtransError {
        Transaction findTransaction = transactionRepository.findById(transactionId).orElseThrow(() -> new EntityNotFoundException(Message.DATA_NOT_FOUND));
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
    public void updateStatusPayment(String orderId, String status) {
        TransactionPayment transactionPayment =getById(orderId);
        Payment payment = transactionPayment.getPayment();
        paymentService.updateStatus(payment, status);

        Transaction transaction = transactionPayment.getTransaction();
        transactionService.updateStatusFromPayment(transaction, status);
        transactionPaymentRepository.saveAndFlush(transactionPayment);
    }
}
