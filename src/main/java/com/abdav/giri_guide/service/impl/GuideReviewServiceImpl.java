package com.abdav.giri_guide.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.abdav.giri_guide.entity.Customer;
import com.abdav.giri_guide.entity.GuideReview;
import com.abdav.giri_guide.entity.TourGuide;
import com.abdav.giri_guide.mapper.GuideReviewMapper;
import com.abdav.giri_guide.model.request.GuideReviewRequest;
import com.abdav.giri_guide.model.response.CommonResponseWithPage;
import com.abdav.giri_guide.model.response.GuideReviewResponse;
import com.abdav.giri_guide.model.response.PagingResponse;
import com.abdav.giri_guide.repository.CustomerRepository;
import com.abdav.giri_guide.repository.GuideReviewRepository;
import com.abdav.giri_guide.repository.TourGuideRepository;
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

    @Override
    public GuideReviewResponse createGuideReview(
            String tourGuideId, GuideReviewRequest request, HttpServletRequest httpReq) {

        Customer customer = customerRepository.findByUserIdAndDeletedDateIsNull(request.userId())
                .orElseThrow(EntityNotFoundException::new);

        TourGuide tourGuide = tourGuideRepository.findByIdAndDeletedDateIsNull(tourGuideId)
                .orElseThrow(EntityNotFoundException::new);

        // TODO: Check transaction entity
        // Optional<GuideReview> review =
        // reviewRepository.findByGuideAndCustomerAndDeletedDateIsNull(
        // tourGuideId, user);

        // if (review.isPresent()) {
        // throw new DataIntegrityViolationException("Customer already reviewed");
        // }

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
        Page<GuideReview> reviews = reviewRepository.findByTourGuideAndDeletedDateIsNull(tourGuide, pageable);

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

}
