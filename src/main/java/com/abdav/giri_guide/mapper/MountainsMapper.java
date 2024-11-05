package com.abdav.giri_guide.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import com.abdav.giri_guide.entity.Mountains;
import com.abdav.giri_guide.model.response.MountainsDetailResponse;
import com.abdav.giri_guide.model.response.MountainsListResponse;
import com.abdav.giri_guide.util.UrlUtil;

import jakarta.servlet.http.HttpServletRequest;

public class MountainsMapper {
    private MountainsMapper() {
    }

    public static MountainsDetailResponse toMountainsDetailResponse(Mountains mountain, HttpServletRequest httpReq) {
        return new MountainsDetailResponse(
                mountain.getId(),
                mountain.getName(),
                mountain.getImage() == null ? null : UrlUtil.resolveImageUrl(mountain.getImage(), httpReq),
                mountain.getCity(),
                mountain.getDescription(),
                mountain.getStatus(),
                mountain.getMessage(),
                mountain.isUseSimaksi(),
                mountain.getPriceSimaksi(),
                mountain.getTips(),
                mountain.getBestTime(),
                HikingPointMapper.toSetHikingPointResponse(mountain.getHikingPoints())

        );
    }

    public static List<MountainsListResponse> toListOfMountain(
            Page<Mountains> mountainsPage, HttpServletRequest httpReq) {

        List<MountainsListResponse> mountainList = new ArrayList<>();
        for (Mountains mountain : mountainsPage.getContent()) {
            mountainList.add(new MountainsListResponse(
                    mountain.getId(),
                    mountain.getName(),
                    mountain.getImage() == null ? null : UrlUtil.resolveImageUrl(mountain.getImage(), httpReq),
                    mountain.getCity(),
                    mountain.getStatus()));
        }
        return mountainList;
    }
}
