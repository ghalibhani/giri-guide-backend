package com.abdav.giri_guide.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.abdav.giri_guide.constant.EDepositStatus;
import com.abdav.giri_guide.constant.ERole;
import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.constant.PathImage;
import com.abdav.giri_guide.entity.Deposit;
import com.abdav.giri_guide.entity.DepositHistory;
import com.abdav.giri_guide.entity.HikingPoint;
import com.abdav.giri_guide.entity.ImageEntity;
import com.abdav.giri_guide.entity.Role;
import com.abdav.giri_guide.entity.TourGuide;
import com.abdav.giri_guide.entity.TourGuideHikingPoint;
import com.abdav.giri_guide.entity.User;
import com.abdav.giri_guide.mapper.TourGuideHikingPointMapper;
import com.abdav.giri_guide.mapper.TourGuideMapper;
import com.abdav.giri_guide.model.request.TourGuideAddHikingPointRequest;
import com.abdav.giri_guide.model.request.TourGuideRequest;
import com.abdav.giri_guide.model.response.CommonResponseWithPage;
import com.abdav.giri_guide.model.response.PagingResponse;
import com.abdav.giri_guide.model.response.TourGuideDetailResponse;
import com.abdav.giri_guide.model.response.TourGuideHikingPointActiveResponse;
import com.abdav.giri_guide.model.response.TourGuideListResponse;
import com.abdav.giri_guide.model.response.TourGuideProfileResponse;
import com.abdav.giri_guide.model.response.TourGuideStatsResponse;
import com.abdav.giri_guide.repository.DepositHistoryRepository;
import com.abdav.giri_guide.repository.DepositRepository;
import com.abdav.giri_guide.repository.HikingPointRepository;
import com.abdav.giri_guide.repository.TourGuideHikingPointRepository;
import com.abdav.giri_guide.repository.TourGuideRepository;
import com.abdav.giri_guide.repository.UserRepository;
import com.abdav.giri_guide.service.ImageService;
import com.abdav.giri_guide.service.RoleService;
import com.abdav.giri_guide.service.TourGuideService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TourGuideServiceImpl implements TourGuideService {
    private final TourGuideRepository tourGuideRepository;
    private final TourGuideHikingPointRepository tourGuideHikingPointRepository;
    private final HikingPointRepository hikingPointRepository;
    private final UserRepository userRepository;
    private final DepositRepository depositRepository;
    private final DepositHistoryRepository depositHistoryRepository;

    private final RoleService roleService;
    private final ImageService imageService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public TourGuideDetailResponse addHikingPoint(
            String tourGuideId,
            TourGuideAddHikingPointRequest request,
            HttpServletRequest httpReq) {

        TourGuide tourGuide = tourGuideRepository.findById(tourGuideId)
                .orElseThrow(EntityNotFoundException::new);
        HikingPoint hikingPoint = hikingPointRepository.findById(request.hikingPointId())
                .orElseThrow(EntityNotFoundException::new);

        Optional<TourGuideHikingPoint> data = tourGuideHikingPointRepository
                .findByTourGuideAndHikingPointAndDeletedDateIsNull(tourGuide, hikingPoint);

        if (data.isPresent()) {
            throw new DataIntegrityViolationException("User Already Add This Hiking Point");
        }

        TourGuideHikingPoint tourGuideHikingPoint = TourGuideHikingPoint.builder()
                .tourGuide(tourGuide)
                .hikingPoint(hikingPoint)
                .build();
        tourGuideHikingPointRepository.save(tourGuideHikingPoint);

        List<TourGuideHikingPoint> hikingPoints = tourGuideHikingPointRepository
                .findByTourGuideAndDeletedDateIsNull(tourGuide);

        return TourGuideMapper.toTourGuideDetailResponse(tourGuide, hikingPoints, httpReq);
    }

    @Override
    public TourGuideProfileResponse createTourGuide(
            MultipartFile image, TourGuideRequest request, HttpServletRequest httpReq) {

        Optional<User> savedUser = userRepository.findByEmail(request.email().trim().toLowerCase());
        if (savedUser.isPresent()) {
            throw new DataIntegrityViolationException("Email Already Registered");
        }

        Optional<TourGuide> savedTourGuide = tourGuideRepository.findByNikAndDeletedDateIsNull(request.nik());
        if (savedTourGuide.isPresent()) {
            throw new DataIntegrityViolationException("NIK Already Registered");
        }

        ImageEntity imageEntity = imageService.create(image, PathImage.PROFILE_PICTURE, request.name());
        Role role = roleService.getOrSaveRole(Role.builder().role(ERole.ROLE_GUIDE).build());
        User user = User.builder()
                .email(request.email().trim().toLowerCase())
                .password(passwordEncoder.encode(request.password()))
                .role(role)
                .build();
        userRepository.saveAndFlush(user);

        TourGuide tourGuide = TourGuide.builder()
                .users(user)
                .name(request.name().trim())
                .gender(request.gender())
                .nik(request.nik().trim())
                .birthDate(request.birthDate())
                .description(request.description().trim())
                .address(request.address())
                .image(imageEntity)
                .maxHiker(request.maxHiker())
                .price(request.price())
                .additionalPrice(request.additionalPrice())
                .totalPorter(request.totalPorter())
                .pricePorter(request.pricePorter())
                .bankAccount(request.bankAccount())
                .build();
        tourGuide = tourGuideRepository.save(tourGuide);

        Deposit deposit = Deposit.builder()
                .tourGuide(tourGuide)
                .build();
        deposit = depositRepository.saveAndFlush(deposit);

        tourGuide.setDeposit(deposit);
        tourGuide = tourGuideRepository.save(tourGuide);

        return TourGuideMapper.toTourGuideProfileResponse(tourGuide, httpReq);
    }

    @Override
    public TourGuideProfileResponse getTourGuideProfile(String userId, HttpServletRequest httpReq) {
        User users = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        TourGuide tourGuide = tourGuideRepository.findByUsersAndDeletedDateIsNull(users)
                .orElseThrow(EntityNotFoundException::new);

        return TourGuideMapper.toTourGuideProfileResponse(tourGuide, httpReq);
    }

    @Override
    public TourGuideDetailResponse getTourGuide(String id, HttpServletRequest httpReq) {

        TourGuide tourGuide = tourGuideRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        List<TourGuideHikingPoint> hikingPoints = tourGuideHikingPointRepository
                .findByTourGuideAndDeletedDateIsNull(tourGuide);
        return TourGuideMapper.toTourGuideDetailResponse(tourGuide, hikingPoints, httpReq);
    }

    @Override
    public CommonResponseWithPage<List<TourGuideListResponse>> getTourGuideList(
            String hikingPointId, Integer size, Integer page, HttpServletRequest httpReq) {
        if (page <= 0) {
            page = 1;
        }

        Pageable pageable = PageRequest.of(page - 1, size);
        if (hikingPointId.equals("")) {
            Page<TourGuide> tourGuidePage = tourGuideRepository.findAllByDeletedDateIsNull(pageable);
            PagingResponse paging = new PagingResponse(
                    page,
                    size,
                    tourGuidePage.getTotalPages(),
                    tourGuidePage.getTotalElements());

            return new CommonResponseWithPage<>(
                    Message.SUCCESS_FETCH,
                    TourGuideMapper.toListOfTourGuideListResponse(tourGuidePage.getContent(),
                            httpReq),
                    paging);

        } else {
            HikingPoint hikingPoint = hikingPointRepository.findById(hikingPointId).orElse(null);
            Page<TourGuideHikingPoint> tourGuidePage = tourGuideHikingPointRepository
                    .findByHikingPointAndDeletedDateIsNull(hikingPoint, pageable);
            PagingResponse paging = new PagingResponse(
                    page,
                    size,
                    tourGuidePage.getTotalPages(),
                    tourGuidePage.getTotalElements());

            return new CommonResponseWithPage<>(
                    Message.SUCCESS_FETCH,
                    TourGuideHikingPointMapper.toListOfTourGuideListResponse(
                            tourGuidePage.getContent(), httpReq),
                    paging);

        }
    }

    @Override
    public TourGuideDetailResponse updateTourGuide(
            String id,
            TourGuideRequest request,
            HttpServletRequest httpReq) {

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
        return TourGuideMapper.toTourGuideDetailResponse(tourGuide, hikingPoints, httpReq);
    }

    @Override
    public TourGuideDetailResponse toggleTourGuideActiveStatus(String id, HttpServletRequest httpReq) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User " + Message.DATA_NOT_FOUND));

        TourGuide tourGuide = tourGuideRepository.findByUsersAndDeletedDateIsNull(user)
                .orElseThrow(() -> new EntityNotFoundException("Tour Guide " + Message.DATA_NOT_FOUND));

        tourGuide.setActive(!tourGuide.isActive());
        tourGuide = tourGuideRepository.save(tourGuide);
        List<TourGuideHikingPoint> hikingPoints = tourGuideHikingPointRepository
                .findByTourGuideAndDeletedDateIsNull(tourGuide);
        return TourGuideMapper.toTourGuideDetailResponse(tourGuide, hikingPoints, httpReq);
    }

    @Override
    public TourGuideDetailResponse updateTourGuideImage(
            String id, MultipartFile image, HttpServletRequest httpReq) {

        TourGuide tourGuide = tourGuideRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tour Guide " + Message.DATA_NOT_FOUND));
        ImageEntity imageEntity = imageService.create(image, PathImage.PROFILE_PICTURE, tourGuide.getName());
        tourGuide.setImage(imageEntity);
        tourGuide = tourGuideRepository.save(tourGuide);
        List<TourGuideHikingPoint> hikingPoints = tourGuideHikingPointRepository
                .findByTourGuideAndDeletedDateIsNull(tourGuide);

        return TourGuideMapper.toTourGuideDetailResponse(tourGuide, hikingPoints, httpReq);
    }

    @Override
    public TourGuideProfileResponse getTourGuideData(String id, HttpServletRequest httpReq) {
        TourGuide tourGuide = tourGuideRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tour Guide " + Message.DATA_NOT_FOUND));
        return TourGuideMapper.toTourGuideProfileResponse(tourGuide, httpReq);
    }

    @Override
    public void softDeleteTourGuide(String id) {
        TourGuide tourGuide = tourGuideRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        tourGuide.setDeletedDate(LocalDateTime.now());
        tourGuideRepository.save(tourGuide);
    }

    @Override
    public List<TourGuideHikingPointActiveResponse> getTourGuideHikingPointActiveList(String userId) {
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        TourGuide tourGuide = tourGuideRepository.findByUsersAndDeletedDateIsNull(user)
                .orElseThrow(EntityNotFoundException::new);
        List<TourGuideHikingPoint> hikingPoints = tourGuideHikingPointRepository
                .findByTourGuideAndDeletedDateIsNull(tourGuide);

        return TourGuideHikingPointMapper.toListOfTourGuideHikingPointActiveResponse(hikingPoints);
    }

    @Override
    public List<TourGuideHikingPointActiveResponse> toggleTourGuideHikingPointActiveList(
            String userId, String id) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User " + Message.DATA_NOT_FOUND));

        TourGuideHikingPoint tourGuideHikingPoint = tourGuideHikingPointRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Hiking Point " + Message.DATA_NOT_FOUND));

        if (!tourGuideHikingPoint.getTourGuide().getUsers().equals(user)) {
            throw new EntityNotFoundException();
        }

        tourGuideHikingPoint.setActive(!tourGuideHikingPoint.isActive());
        tourGuideHikingPointRepository.save(tourGuideHikingPoint);

        List<TourGuideHikingPoint> hikingPoints = tourGuideHikingPointRepository
                .findByTourGuideAndDeletedDateIsNull(tourGuideHikingPoint.getTourGuide());

        return TourGuideHikingPointMapper.toListOfTourGuideHikingPointActiveResponse(hikingPoints);
    }

    @Override
    public TourGuideStatsResponse getTourGuideStats(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User " + Message.DATA_NOT_FOUND));

        TourGuide tourGuide = tourGuideRepository.findByUsersAndDeletedDateIsNull(user)
                .orElseThrow(() -> new EntityNotFoundException("Tour Guide " + Message.DATA_NOT_FOUND));

        List<DepositHistory> histories;
        if (tourGuide.getDeposit() == null) {
            histories = new ArrayList<>();
        } else {
            LocalDateTime today = LocalDateTime.now();
            int monthLength = LocalDate.now().lengthOfMonth();
            histories = depositHistoryRepository.findByDepositAndStatusInAndCreatedDateBetween(
                    tourGuide.getDeposit(),
                    new ArrayList<>(List.of(EDepositStatus.IN, EDepositStatus.OUT)),
                    today.withDayOfMonth(1),
                    today.withDayOfMonth(monthLength));
        }

        return TourGuideMapper.toTourGuideStatsResponse(tourGuide, histories);
    }

    @Override
    public void softDeleteTourGuideHikingPoint(String tourGuideId, String hikingPointId) {

        TourGuide tourGuide = tourGuideRepository.findById(tourGuideId)
                .orElseThrow(() -> new EntityNotFoundException("Tour Guide " + Message.DATA_NOT_FOUND));

        HikingPoint hikingPoint = hikingPointRepository.findById(hikingPointId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Hiking Point " + Message.DATA_NOT_FOUND));

        TourGuideHikingPoint tourGuideHikingPoint = tourGuideHikingPointRepository
                .findByTourGuideAndHikingPointAndDeletedDateIsNull(tourGuide, hikingPoint)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Tour Guide Hiking Point " + Message.DATA_NOT_FOUND));

        tourGuideHikingPoint.setDeletedDate(LocalDateTime.now());
        tourGuideHikingPointRepository.save(tourGuideHikingPoint);

    }

}
