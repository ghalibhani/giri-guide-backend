package com.abdav.giri_guide.service;

import org.springframework.data.domain.Page;

import com.abdav.giri_guide.entity.Mountains;
import com.abdav.giri_guide.model.response.MountainsDetailResponse;
import com.abdav.giri_guide.model.response.MountainsListResponse;

public interface MountainsService {
    Page<MountainsListResponse> mountainList(String city);

    MountainsDetailResponse mountainDetail(String id);

    MountainsDetailResponse updateMountain(String id, Mountains updatedMountains);

    MountainsDetailResponse createMountain(Mountains mountains);

    void deleteMountain(String id);
}
