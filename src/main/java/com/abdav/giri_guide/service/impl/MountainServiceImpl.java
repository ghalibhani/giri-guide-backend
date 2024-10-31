package com.abdav.giri_guide.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.abdav.giri_guide.entity.HikingPoint;
import com.abdav.giri_guide.entity.Mountains;
import com.abdav.giri_guide.model.request.HikingPointRequest;
import com.abdav.giri_guide.model.request.MountainsRequest;
import com.abdav.giri_guide.model.response.HikingPointResponse;
import com.abdav.giri_guide.model.response.MountainsDetailResponse;
import com.abdav.giri_guide.model.response.MountainsListResponse;
import com.abdav.giri_guide.repository.HikingPointRepository;
import com.abdav.giri_guide.repository.MountainsRepository;
import com.abdav.giri_guide.service.MountainsService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MountainServiceImpl implements MountainsService {
    private final MountainsRepository mountainRepository;
    private final HikingPointRepository hikingPointRepository;

    @Override
    public MountainsDetailResponse createMountain(MountainsRequest newMountains) {
        Optional<Mountains> savedMountain = mountainRepository
                .findByNameIgnoreCaseAndDeletedDateIsNull(newMountains.name().trim());
        if (savedMountain.isPresent()) {
            throw new DataIntegrityViolationException("Active data with same name already exist");
        }

        Mountains mountains = mountainRepository.save(newMountains.toMountains());
        return new MountainsDetailResponse(
                mountains.getId(),
                mountains.getName(),
                mountains.getImage(),
                mountains.getCity(),
                mountains.getDescription(),
                mountains.getStatus(),
                mountains.getMessage(),
                toSetHikingPointResponse(hikingPointRepository.findByMountainAndDeletedDateIsNull(mountains))

        );
    }

    @Override
    public void deleteMountain(String id) {
        Mountains mountain = mountainRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        mountain.setDeletedDate(LocalDateTime.now());
        mountainRepository.save(mountain);
    }

    @Override
    public MountainsDetailResponse mountainDetail(String id) {
        Mountains mountain = mountainRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        return new MountainsDetailResponse(
                mountain.getId(),
                mountain.getName(),
                mountain.getImage(),
                mountain.getCity(),
                mountain.getDescription(),
                mountain.getStatus(),
                mountain.getMessage(),
                toSetHikingPointResponse(hikingPointRepository.findByMountainAndDeletedDateIsNull(mountain))

        );
    }

    @Override
    public Page<MountainsListResponse> mountainList(String city, Integer page, Integer size) {
        if (page <= 0) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        return mountainRepository.findAllByDeletedDateIsNull(pageable);
    }

    @Override
    public MountainsDetailResponse updateMountain(String id, MountainsRequest updatedMountains) {
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
        mountainRepository.save(mountain);

        return new MountainsDetailResponse(
                mountain.getId(),
                mountain.getName(),
                mountain.getImage(),
                mountain.getCity(),
                mountain.getDescription(),
                mountain.getStatus(),
                mountain.getMessage(),
                toSetHikingPointResponse(hikingPointRepository.findByMountainAndDeletedDateIsNull(mountain))

        );
    }

    public HikingPointResponse createHikingPoint(String mountainId, HikingPointRequest request) {
        Mountains mountain = mountainRepository.findById(mountainId).orElseThrow(EntityNotFoundException::new);
        HikingPoint hikingPoint = HikingPoint.builder()
                .mountain(mountain)
                .name(request.name().trim())
                .coordinate(request.coordinate())
                .build();

        hikingPoint = hikingPointRepository.save(hikingPoint);

        return new HikingPointResponse(
                hikingPoint.getId(),
                hikingPoint.getMountain().getId(),
                hikingPoint.getName(),
                hikingPoint.getCoordinate());
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
                    hikingPoint.getCoordinate()));
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
                hikingPoint.getCoordinate());
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
                hikingPoint.getCoordinate());
    }

}
