package com.abdav.giri_guide.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abdav.giri_guide.constant.PathApi;
import com.abdav.giri_guide.model.request.HikingPointRequest;
import com.abdav.giri_guide.model.request.MountainsRequest;
import com.abdav.giri_guide.model.response.CommonResponse;
import com.abdav.giri_guide.model.response.CommonResponseWithPage;
import com.abdav.giri_guide.model.response.MountainsListResponse;
import com.abdav.giri_guide.model.response.PagingResponse;
import com.abdav.giri_guide.service.MountainsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(PathApi.MOUNTAINS_API)
public class MountainController {
    private final MountainsService service;

    @GetMapping("")
    public ResponseEntity<CommonResponseWithPage<?>> getMountainList(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size

    ) {
        Page<MountainsListResponse> mountainList = service.mountainList(null, page, size);
        PagingResponse paging = new PagingResponse(1, 5, mountainList.getTotalPages(),
                mountainList.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponseWithPage<>(
                        "Data fetched",
                        mountainList.getContent(),
                        paging));
    }

    @PostMapping("")
    public ResponseEntity<CommonResponse<?>> createMountain(@RequestBody @Validated MountainsRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>("Mountain data successfully created",
                        service.createMountain(request)));
    }

    @GetMapping("{id}")
    public ResponseEntity<CommonResponse<?>> getMountainDetail(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>("Data fetched successfully",
                        service.mountainDetail(id)));
    }

    @PutMapping("{id}")
    public ResponseEntity<CommonResponse<?>> updateMountain(
            @PathVariable String id,
            @RequestBody MountainsRequest request) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>("Data updated successfully",
                        service.updateMountain(id, request)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CommonResponse<?>> deleteMountain(@PathVariable String id) {
        service.deleteMountain(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>("Data deleted successfully", null));
    }

    @GetMapping("{mountainId}/hiking-points")
    public ResponseEntity<?> getHikingPoints(@PathVariable String mountainId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>("Data fetched", service.getHikingPoints(mountainId)));
    }

    @PostMapping("{mountainId}/hiking-points")
    public ResponseEntity<?> addHikingPoint(@PathVariable String mountainId,
            @RequestBody @Validated HikingPointRequest request

    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>("Data Create", service.createHikingPoint(mountainId, request)));
    }

    @GetMapping("hiking-points/{id}")
    public ResponseEntity<?> getHikingPoint(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>("Data Fetched", service.getHikingPoint(id)));
    }

    @PutMapping("hiking-points/{id}")
    public ResponseEntity<?> updateHikingPoint(@PathVariable String id, @RequestBody HikingPointRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>("Data Updated", service.updateHikingPoint(id, request)));
    }

    @DeleteMapping("hiking-points/{id}")
    public ResponseEntity<?> deleteHikingPoint(@PathVariable String id) {
        service.deleteHikingPoint(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>("Data Deleted", null));
    }
}
