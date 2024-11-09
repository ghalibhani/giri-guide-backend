package com.abdav.giri_guide.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.entity.Customer;
import com.abdav.giri_guide.entity.GuideReview;
import com.abdav.giri_guide.entity.TourGuide;
import com.abdav.giri_guide.entity.Transaction;
import com.abdav.giri_guide.mapper.GuideReviewMapper;
import com.abdav.giri_guide.model.request.GuideReviewPutRequest;
import com.abdav.giri_guide.model.request.GuideReviewRequest;
import com.abdav.giri_guide.model.response.CommonResponseWithPage;
import com.abdav.giri_guide.model.response.GuideReviewResponse;
import com.abdav.giri_guide.model.response.PagingResponse;
import com.abdav.giri_guide.repository.CustomerRepository;
import com.abdav.giri_guide.repository.GuideReviewRepository;
import com.abdav.giri_guide.repository.TourGuideRepository;
import com.abdav.giri_guide.repository.TransactionRepository;
import com.abdav.giri_guide.service.GuideReviewService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GuideReviewServiceImpl implements GuideReviewService {
    private final GuideReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final TourGuideRepository tourGuideRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public GuideReviewResponse createGuideReview(
            String tourGuideId, GuideReviewRequest request, HttpServletRequest httpReq) {

        Customer customer = customerRepository.findByUserIdAndDeletedDateIsNull(request.userId())
                .orElseThrow(EntityNotFoundException::new);

        TourGuide tourGuide = tourGuideRepository.findByIdAndDeletedDateIsNull(tourGuideId)
                .orElseThrow(EntityNotFoundException::new);

        GuideReview newReview = GuideReview.builder()
                .customer(customer)
                .tourGuide(tourGuide)
                .transaction(null)
                .rating(request.rating())
                .usePorter(request.usePorter())
                .review(request.review())
                .build();
        newReview = reviewRepository.save(newReview);

        return GuideReviewMapper.toGuideReviewResponse(newReview, httpReq);
    }

    @Override
    public GuideReviewResponse getGuideReviewById(String id, HttpServletRequest httpReq) {

        GuideReview review = reviewRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return GuideReviewMapper.toGuideReviewResponse(review, httpReq);
    }

    @Override
    public CommonResponseWithPage<List<GuideReviewResponse>> getGuideReviewsByGuideId(
            String tourGuideId, Integer page, Integer size, HttpServletRequest httpReq) {

        if (page <= 0) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, size);

        TourGuide tourGuide = tourGuideRepository.findByIdAndDeletedDateIsNull(tourGuideId)
                .orElseThrow(EntityNotFoundException::new);
        Page<GuideReview> reviews = reviewRepository
                .findByTourGuideAndReviewIsNotNullAndDeletedDateIsNull(tourGuide, pageable);

        PagingResponse paging = new PagingResponse(
                page,
                size,
                reviews.getTotalPages(),
                reviews.getTotalElements());

        List<GuideReviewResponse> reviewList = new ArrayList<>();
        for (GuideReview review : reviews.getContent()) {
            reviewList.add(GuideReviewMapper.toGuideReviewResponse(review, httpReq));
        }

        return new CommonResponseWithPage<>("Data Fetched", reviewList, paging);
    }

    @Override
    public GuideReview createBlankReview(Transaction transaction) {
        GuideReview review = GuideReview.builder()
                .customer(transaction.getCustomer())
                .tourGuide(transaction.getTourGuide())
                .transaction(transaction)
                .usePorter(transaction.getPorterQty() > 0)
                .rating(5)
                .review(null)
                .build();

        return reviewRepository.save(review);

    }

    @Override
    public GuideReviewResponse putReviewOnTransaction(
            String transactionId, GuideReviewPutRequest request, HttpServletRequest httpReq) {

        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(
                () -> new EntityNotFoundException("Transaction " + Message.DATA_NOT_FOUND));

        GuideReview review = reviewRepository.findByTransactionAndDeletedDateIsNull(transaction)
                .orElse(createBlankReview(transaction));

        if (review.getReview() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already reviewd");
        }

        review.setRating(request.rating());
        review.setReview(request.review());
        reviewRepository.save(review);

        return GuideReviewMapper.toGuideReviewResponse(review, httpReq);
    }

    @Override
    public GuideReview getGuideReviewByTransactionId(String transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(
                () -> new EntityNotFoundException("Transaction " + Message.DATA_NOT_FOUND));

        GuideReview review = reviewRepository.findByTransactionAndDeletedDateIsNull(transaction)
                .orElse(null);

        return review;
    }
}
