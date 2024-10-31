package com.abdav.giri_guide.service.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.abdav.giri_guide.entity.Mountains;
import com.abdav.giri_guide.model.request.MountainsRequest;
import com.abdav.giri_guide.model.response.MountainsDetailResponse;
import com.abdav.giri_guide.model.response.MountainsListResponse;
import com.abdav.giri_guide.repository.MountainsRepository;
import com.abdav.giri_guide.service.MountainsService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MountainServiceImpl implements MountainsService {
    private final MountainsRepository repository;

    @Override
    public MountainsDetailResponse createMountain(MountainsRequest newMountains) {
        Mountains mountains = repository.save(newMountains.toMountains());
        return new MountainsDetailResponse(
                mountains.getId(),
                mountains.getName(),
                mountains.getImage(),
                mountains.getCity(),
                mountains.getDescription(),
                mountains.getStatus(),
                mountains.getMessage()

        );
    }

    @Override
    public void deleteMountain(String id) {
        Mountains mountain = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        mountain.setDeletedDate(LocalDateTime.now());
        repository.save(mountain);
    }

    @Override
    public MountainsDetailResponse mountainDetail(String id) {
        Mountains mountain = repository.findById(id).orElseThrow(EntityNotFoundException::new);

        return new MountainsDetailResponse(
                mountain.getId(),
                mountain.getName(),
                mountain.getImage(),
                mountain.getCity(),
                mountain.getDescription(),
                mountain.getStatus(),
                mountain.getMessage()

        );
    }

    @Override
    public Page<MountainsListResponse> mountainList(String city, Integer page, Integer size) {
        if (page <= 0) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        return repository.findAllByDeletedDateIsNull(pageable);
    }

    @Override
    public MountainsDetailResponse updateMountain(String id, MountainsRequest updatedMountains) {
        Mountains mountain = repository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (updatedMountains.name() != null) {
            mountain.setName(updatedMountains.name());
        }
        if (updatedMountains.city() != null) {
            mountain.setCity(updatedMountains.city());
        }
        if (updatedMountains.description() != null) {
            mountain.setCity(updatedMountains.description());
        }
        if (updatedMountains.status() != null) {
            mountain.setStatus(updatedMountains.statusToEnum());
        }
        if (updatedMountains.message() != null) {
            mountain.setMessage(updatedMountains.message());
        }
        // mountain.setLastModifiedDate(LocalDateTime.now());
        repository.save(mountain);

        return new MountainsDetailResponse(
                mountain.getId(),
                mountain.getName(),
                mountain.getImage(),
                mountain.getCity(),
                mountain.getDescription(),
                mountain.getStatus(),
                mountain.getMessage()

        );
    }
}
