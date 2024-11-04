package com.abdav.giri_guide.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.abdav.giri_guide.constant.PathImage;
import com.abdav.giri_guide.entity.HikingPoint;
import com.abdav.giri_guide.entity.ImageEntity;
import com.abdav.giri_guide.entity.TourGuide;
import com.abdav.giri_guide.entity.TourGuideHikingPoint;
import com.abdav.giri_guide.mapper.TourGuideMapper;
import com.abdav.giri_guide.model.request.TourGuideAddHikingPointRequest;
import com.abdav.giri_guide.model.request.TourGuideRequest;
import com.abdav.giri_guide.model.response.CommonResponseWithPage;
import com.abdav.giri_guide.model.response.TourGuideDetailResponse;
import com.abdav.giri_guide.model.response.TourGuideListResponse;
import com.abdav.giri_guide.repository.HikingPointRepository;
import com.abdav.giri_guide.repository.TourGuideHikingPointRepository;
import com.abdav.giri_guide.repository.TourGuideRepository;
import com.abdav.giri_guide.service.ImageService;
import com.abdav.giri_guide.service.TourGuideService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TourGuideServiceImpl implements TourGuideService {
    private final TourGuideRepository tourGuideRepository;
    private final TourGuideHikingPointRepository tourGuideHikingPointRepository;
    private final HikingPointRepository hikingPointRepository;

    private final ImageService imageService;

    @Override
    public TourGuideDetailResponse addHikingPoint(String tourGuideId, TourGuideAddHikingPointRequest request) {
        // TODO Check User before create
        TourGuide tourGuide = tourGuideRepository.findById(tourGuideId).orElseThrow(EntityNotFoundException::new);
        HikingPoint hikingPoint = hikingPointRepository.findById(request.hikingPointId())
                .orElseThrow(EntityNotFoundException::new);

        TourGuideHikingPoint tourGuideHikingPoint = TourGuideHikingPoint.builder()
                .tourGuide(tourGuide)
                .hikingPoint(hikingPoint)
                .build();
        tourGuideHikingPointRepository.save(tourGuideHikingPoint);
        List<TourGuideHikingPoint> hikingPoints = tourGuideHikingPointRepository
                .findByTourGuideAndDeletedDateIsNull(tourGuide);

        return TourGuideMapper.toTourGuideDetailResponse(tourGuide, hikingPoints);
    }

    @Override
    public TourGuideDetailResponse createTourGuide(MultipartFile image, TourGuideRequest request) {
        // TODO check UserID before create tour guide account
        Optional<TourGuide> savedTourGuide = tourGuideRepository.findByNikAndDeletedDateIsNull(request.nik());
        if (savedTourGuide.isPresent()) {
            throw new DataIntegrityViolationException("Active data already exist");
        }

        ImageEntity imageEntity = imageService.create(image, PathImage.PROFILE_PICTURE, request.name());
        TourGuide tourGuide = TourGuide.builder()
                .name(request.name())
                .nik(request.nik())
                .description(request.description())
                .image(imageEntity)
                .maxHiker(request.maxHiker())
                .price(request.price())
                .additionalPrice(request.additionalPrice())
                .totalPorter(request.totalPorter())
                .pricePorter(request.pricePorter())
                .build();

        tourGuide = tourGuideRepository.save(tourGuide);
        List<TourGuideHikingPoint> hikingPoints = tourGuideHikingPointRepository
                .findByTourGuideAndDeletedDateIsNull(tourGuide);

        return TourGuideMapper.toTourGuideDetailResponse(tourGuide, hikingPoints);
    }

    @Override
    public TourGuideDetailResponse getTourGuide(String id) {
        TourGuide tourGuide = tourGuideRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        List<TourGuideHikingPoint> hikingPoints = tourGuideHikingPointRepository
                .findByTourGuideAndDeletedDateIsNull(tourGuide);
        return TourGuideMapper.toTourGuideDetailResponse(tourGuide, hikingPoints);
    }

    @Override
    public CommonResponseWithPage<List<TourGuideListResponse>> getTourGuideList(String hikingPointId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TourGuideDetailResponse updateTourGuide(String id, TourGuideRequest request) {
        TourGuide tourGuide = tourGuideRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (request.name() != null) {
            tourGuide.setName(request.name());
        }
        if (request.nik() != null) {
            tourGuide.setNik(request.nik());
        }
        if (request.description() != null) {
            tourGuide.setDescription(request.description());
        }
        if (request.maxHiker() != null) {
            tourGuide.setMaxHiker(request.maxHiker());
        }
        if (request.price() != null) {
            tourGuide.setPrice(request.price());
        }
        if (request.additionalPrice() != null) {
            tourGuide.setAdditionalPrice(request.additionalPrice());
        }
        if (request.totalPorter() != null) {
            tourGuide.setTotalPorter(request.totalPorter());
        }
        if (request.pricePorter() != null) {
            tourGuide.setPricePorter(request.pricePorter());
        }

        tourGuide = tourGuideRepository.save(tourGuide);
        List<TourGuideHikingPoint> hikingPoints = tourGuideHikingPointRepository
                .findByTourGuideAndDeletedDateIsNull(tourGuide);
        return TourGuideMapper.toTourGuideDetailResponse(tourGuide, hikingPoints);
    }

    @Override
    public TourGuideDetailResponse toggleTourGuideActiveStatus(String id) {
        TourGuide tourGuide = tourGuideRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        tourGuide.setActive(!tourGuide.isActive());
        tourGuide = tourGuideRepository.save(tourGuide);
        List<TourGuideHikingPoint> hikingPoints = tourGuideHikingPointRepository
                .findByTourGuideAndDeletedDateIsNull(tourGuide);
        return TourGuideMapper.toTourGuideDetailResponse(tourGuide, hikingPoints);
    }

    @Override
    public TourGuideDetailResponse updateTourGuideImage(String id, MultipartFile image) {
        TourGuide tourGuide = tourGuideRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        ImageEntity imageEntity = imageService.create(image, PathImage.PROFILE_PICTURE, tourGuide.getName());
        tourGuide.setImage(imageEntity);
        tourGuide = tourGuideRepository.save(tourGuide);
        List<TourGuideHikingPoint> hikingPoints = tourGuideHikingPointRepository
                .findByTourGuideAndDeletedDateIsNull(tourGuide);

        return TourGuideMapper.toTourGuideDetailResponse(tourGuide, hikingPoints);
    }

    @Override
    public void softDeleteTourGuide(String id) {
        TourGuide tourGuide = tourGuideRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        tourGuide.setDeletedDate(LocalDateTime.now());
        tourGuideRepository.save(tourGuide);
    }

}
