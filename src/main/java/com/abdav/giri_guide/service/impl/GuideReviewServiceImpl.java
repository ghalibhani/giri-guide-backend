package com.abdav.giri_guide.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.abdav.giri_guide.entity.GuideReview;
import com.abdav.giri_guide.entity.Mountains;
import com.abdav.giri_guide.model.request.GuideReviewRequest;
import com.abdav.giri_guide.model.response.CommonResponseWithPage;
import com.abdav.giri_guide.model.response.GuideReviewResponse;
import com.abdav.giri_guide.model.response.MountainsListResponse;
import com.abdav.giri_guide.model.response.PagingResponse;
import com.abdav.giri_guide.repository.GuideReviewRepository;
import com.abdav.giri_guide.service.GuideReviewService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GuideReviewServiceImpl implements GuideReviewService {
    private final GuideReviewRepository reviewRepository;

    @Override
    public GuideReviewResponse createGuideReview(String guideId, GuideReviewRequest request) {
        Optional<GuideReview> review = reviewRepository.findByGuideAndCustomerAndDeletedDateIsNull(
                guideId, request.customerId());

        if (review.isPresent()) {
            throw new DataIntegrityViolationException("Customer already reviewed");
        }
        GuideReview newReview = GuideReview.builder()
                .customer(request.customerId())
                .guide(guideId)
                .rating(request.rating())
                .usePorter(request.usePorter())
                .review(request.review())
                .build();

        newReview = reviewRepository.save(newReview);
        return new GuideReviewResponse(
                newReview.getId(),
                newReview.getCustomer(),
                "fufufafa",
                newReview.getCreatedDate(),
                newReview.getUsePorter(),
                newReview.getRating(),
                newReview.getReview(),
                "gambar");
    }

    @Override
    public GuideReviewResponse getGuideReviewById(String id) {
        GuideReview review = reviewRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return new GuideReviewResponse(
                review.getId(),
                review.getCustomer(),
                "fufufafa",
                review.getCreatedDate(),
                review.getUsePorter(),
                review.getRating(),
                review.getReview(),
                "gambar");
    }

    @Override
    public CommonResponseWithPage<List<GuideReviewResponse>> getGuideReviewsByGuideId(
            String id, Integer page, Integer size) {

        if (page <= 0) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<GuideReview> reviews = reviewRepository.findByGuideAndDeletedDateIsNull(id, pageable);

        PagingResponse paging = new PagingResponse(
                page,
                size,
                reviews.getTotalPages(),
                reviews.getTotalElements());

        List<GuideReviewResponse> reviewList = new ArrayList<>();
        for (GuideReview review : reviews.getContent()) {
            reviewList.add(new GuideReviewResponse(
                    review.getId(),
                    review.getCustomer(),
                    "fufufafa",
                    review.getCreatedDate(),
                    review.getUsePorter(),
                    review.getRating(),
                    review.getReview(),
                    "gambar"));
        }

        return new CommonResponseWithPage<>("Data Fetched", reviewList, paging);
    }

}
