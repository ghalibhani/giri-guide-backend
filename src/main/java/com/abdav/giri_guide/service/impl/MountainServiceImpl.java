package com.abdav.giri_guide.service.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.abdav.giri_guide.entity.Mountains;
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
    public MountainsDetailResponse createMountain(Mountains mountains) {
        Mountains savedMountains = repository.save(mountains);
        return new MountainsDetailResponse(
                savedMountains.getId(),
                savedMountains.getName(),
                savedMountains.getImage(),
                savedMountains.getCity(),
                savedMountains.getDescription());
    }

    @Override
    public void deleteMountain(String id) {
        Mountains mountain = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        mountain.setDeletedDate(LocalDateTime.now());
    }

    @Override
    public MountainsDetailResponse mountainDetail(String id) {
        Mountains mountain = repository.findById(id).orElseThrow(EntityNotFoundException::new);

        return new MountainsDetailResponse(
                mountain.getId(),
                mountain.getName(),
                mountain.getImage(),
                mountain.getCity(),
                mountain.getDescription()

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
    public MountainsDetailResponse updateMountain(String id, Mountains updatedMountains) {
        Mountains mountain = repository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (updatedMountains.getName() != null) {
            mountain.setName(updatedMountains.getName());
        }
        if (updatedMountains.getCity() != null) {
            mountain.setCity(updatedMountains.getCity());
        }
        if (updatedMountains.getDescription() != null) {
            mountain.setCity(updatedMountains.getDescription());
        }
        mountain.setLastModifiedDate(LocalDateTime.now());
        repository.save(mountain);

        return new MountainsDetailResponse(
                mountain.getId(),
                mountain.getName(),
                mountain.getImage(),
                mountain.getCity(),
                mountain.getDescription()

        );
    }
}
