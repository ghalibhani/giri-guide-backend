package com.abdav.giri_guide.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.abdav.giri_guide.entity.HikingPoint;
import com.abdav.giri_guide.entity.ImageEntity;
import com.abdav.giri_guide.entity.Mountains;
import com.abdav.giri_guide.mapper.MountainsMapper;
import com.abdav.giri_guide.model.request.HikingPointRequest;
import com.abdav.giri_guide.model.request.MountainsRequest;
import com.abdav.giri_guide.model.response.CommonResponseWithPage;
import com.abdav.giri_guide.model.response.HikingPointResponse;
import com.abdav.giri_guide.model.response.MountainsDetailResponse;
import com.abdav.giri_guide.model.response.MountainsListResponse;
import com.abdav.giri_guide.model.response.PagingResponse;
import com.abdav.giri_guide.repository.HikingPointRepository;
import com.abdav.giri_guide.repository.MountainsRepository;
import com.abdav.giri_guide.service.ImageService;
import com.abdav.giri_guide.service.MountainsService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MountainServiceImpl implements MountainsService {
    private final MountainsRepository mountainRepository;
    private final HikingPointRepository hikingPointRepository;

    private final ImageService imageService;

    @Override
    public MountainsDetailResponse createMountain(
            MountainsRequest newMountains, MultipartFile requestImage, HttpServletRequest httpReq) {

        Optional<Mountains> savedMountain = mountainRepository
                .findByNameIgnoreCaseAndDeletedDateIsNull(newMountains.name().trim());
        if (savedMountain.isPresent()) {
            throw new DataIntegrityViolationException("Active data with same name already exist");
        }

        ImageEntity image = imageService.create(requestImage, "/images/mountain", newMountains.name());
        Mountains mountains = newMountains.toMountains();
        mountains.setImage(image);
        mountainRepository.save(mountains);
        return MountainsMapper.toMountainsDetailResponse(mountains, httpReq);
    }

    @Override
    public void deleteMountain(String id) {
        Mountains mountain = mountainRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        mountain.setDeletedDate(LocalDateTime.now());
        mountainRepository.save(mountain);
    }

    @Override
    public MountainsDetailResponse mountainDetail(String id, HttpServletRequest httpReq) {
        Mountains mountain = mountainRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return MountainsMapper.toMountainsDetailResponse(mountain, httpReq);
    }

    @Override
    public CommonResponseWithPage<List<MountainsListResponse>> mountainList(
            String name, String city, Integer page, Integer size, HttpServletRequest httpReq) {

        if (page <= 0) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Mountains> mountainsPage = mountainRepository
                .findAllByNameContainingIgnoreCaseAndCityContainingIgnoreCaseAndDeletedDateIsNull(name, city, pageable);

        PagingResponse paging = new PagingResponse(
                page,
                size,
                mountainsPage.getTotalPages(),
                mountainsPage.getTotalElements());

        List<MountainsListResponse> mountainList = MountainsMapper.toListOfMountain(mountainsPage, httpReq);
        return new CommonResponseWithPage<>("Data Fetched", mountainList, paging);
    }

    @Override
    public MountainsDetailResponse updateMountain(
            String id, MountainsRequest updatedMountains, HttpServletRequest httpReq) {

        // TODO check mountain checker for new data
        Mountains mountain = mountainRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        Optional<Mountains> savedMountain = mountainRepository
                .findByNameIgnoreCaseAndDeletedDateIsNull(updatedMountains.name().trim());
        if (savedMountain.isPresent() && !mountain.equals(savedMountain.get())) {
            throw new DataIntegrityViolationException("Active data with same name already exist");
        }

        if (updatedMountains.name() != null) {
            mountain.setName(updatedMountains.name().trim());
        }
        if (updatedMountains.city() != null) {
            mountain.setCity(updatedMountains.city().trim());
        }
        if (updatedMountains.description() != null) {
            mountain.setCity(updatedMountains.description().trim());
        }
        if (updatedMountains.status() != null) {
            mountain.setStatus(updatedMountains.statusToEnum());
        }
        if (updatedMountains.message() != null) {
            mountain.setMessage(updatedMountains.message().trim());
        }
        if (updatedMountains.priceSimaksi() != null) {
            mountain.setPriceSimaksi(updatedMountains.priceSimaksi());
        }
        mountainRepository.save(mountain);

        return MountainsMapper.toMountainsDetailResponse(mountain, httpReq);
    }

    public HikingPointResponse createHikingPoint(String mountainId, HikingPointRequest request) {
        Mountains mountain = mountainRepository.findById(mountainId).orElseThrow(EntityNotFoundException::new);
        HikingPoint hikingPoint = HikingPoint.builder()
                .mountain(mountain)
                .name(request.name().trim())
                .coordinate(request.coordinate())
                .price(request.price())
                .build();

        hikingPoint = hikingPointRepository.save(hikingPoint);

        return new HikingPointResponse(
                hikingPoint.getId(),
                hikingPoint.getMountain().getId(),
                hikingPoint.getName(),
                hikingPoint.getCoordinate(),
                hikingPoint.getPrice());
    }

    public Set<HikingPointResponse> getHikingPoints(String mountainId) {
        Mountains mountain = mountainRepository.findById(mountainId).orElseThrow(EntityNotFoundException::new);
        return toSetHikingPointResponse(hikingPointRepository.findByMountainAndDeletedDateIsNull(mountain));
    }

    private Set<HikingPointResponse> toSetHikingPointResponse(List<HikingPoint> hikingPoints) {
        Set<HikingPointResponse> result = new HashSet<>();
        for (HikingPoint hikingPoint : hikingPoints) {
            result.add(new HikingPointResponse(
                    hikingPoint.getId(),
                    hikingPoint.getMountain().getId(),
                    hikingPoint.getName(),
                    hikingPoint.getCoordinate(),
                    hikingPoint.getPrice()));
        }
        return result;
    }

    @Override
    public void deleteHikingPoint(String id) {
        HikingPoint hikingPoint = hikingPointRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        hikingPoint.setDeletedDate(LocalDateTime.now());
        hikingPointRepository.save(hikingPoint);
    }

    @Override
    public HikingPointResponse getHikingPoint(String id) {
        HikingPoint hikingPoint = hikingPointRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return new HikingPointResponse(
                hikingPoint.getId(),
                hikingPoint.getMountain().getId(),
                hikingPoint.getName(),
                hikingPoint.getCoordinate(),
                hikingPoint.getPrice());
    }

    @Override
    public HikingPointResponse updateHikingPoint(String id, HikingPointRequest request) {
        HikingPoint hikingPoint = hikingPointRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (request.name() != null) {
            hikingPoint.setName(request.name().trim());
        }
        if (request.coordinate() != null) {
            hikingPoint.setCoordinate(request.coordinate());
        }
        hikingPointRepository.save(hikingPoint);

        return new HikingPointResponse(
                hikingPoint.getId(),
                hikingPoint.getMountain().getId(),
                hikingPoint.getName(),
                hikingPoint.getCoordinate(),
                hikingPoint.getPrice());
    }

}
