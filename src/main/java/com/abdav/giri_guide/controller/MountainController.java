package com.abdav.giri_guide.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.constant.PathApi;
import com.abdav.giri_guide.model.request.HikingPointRequest;
import com.abdav.giri_guide.model.request.MountainsRequest;
import com.abdav.giri_guide.model.response.CommonResponse;
import com.abdav.giri_guide.service.MountainsService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(PathApi.MOUNTAINS_API)
@SecurityRequirement(name = "bearerAuth")
public class MountainController {
    private final MountainsService service;

    @PreAuthorize("hasAnyRole('CUSTOMER', 'GUIDE', 'ADMIN')")
    @GetMapping("")
    public ResponseEntity<?> getMountainList(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String city,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            HttpServletRequest httpReq

    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.mountainList(name, city, page, size, httpReq));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createMountain(
            @RequestParam String name,
            @RequestParam String city,
            @RequestParam String description,
            @RequestParam String status,
            @RequestParam String message,
            @RequestParam boolean useSimaksi,
            @RequestParam Long priceSimaksi,
            @RequestParam MultipartFile image,
            @RequestParam String tips,
            @RequestParam String bestTime,
            HttpServletRequest httpReq

    ) {
        MountainsRequest request = new MountainsRequest(
                name, city, description, status, message, useSimaksi, priceSimaksi, tips, bestTime);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>("Mountain data successfully created",
                        service.createMountain(request, image, httpReq)));
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'GUIDE', 'ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<CommonResponse<?>> getMountainDetail(@PathVariable String id,
            HttpServletRequest httpReq) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(Message.SUCCESS_FETCH,
                        service.mountainDetail(id, httpReq)));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<?> updateMountain(
            @PathVariable String id,
            @RequestBody MountainsRequest request,
            HttpServletRequest httpReq

    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(Message.DATA_UPDATED,
                        service.updateMountain(id, request, httpReq)));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping(path = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateMountainImage(
            @PathVariable String id,
            @RequestParam MultipartFile image,
            HttpServletRequest httpReq

    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(Message.DATA_UPDATED,
                        service.updateMountainImage(id, image, httpReq)));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<CommonResponse<?>> deleteMountain(@PathVariable String id) {
        service.deleteMountain(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>("Data deleted successfully", null));
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'GUIDE', 'ADMIN')")
    @GetMapping("{mountainId}/hiking-points")
    public ResponseEntity<?> getHikingPoints(@PathVariable String mountainId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>("Data fetched", service.getHikingPoints(mountainId)));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("{mountainId}/hiking-points")
    public ResponseEntity<?> addHikingPoint(@PathVariable String mountainId,
            @RequestBody @Validated HikingPointRequest request

    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>("Data Create",
                        service.createHikingPoint(mountainId, request)));
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'GUIDE', 'ADMIN')")
    @GetMapping("hiking-points/{id}")
    public ResponseEntity<?> getHikingPoint(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>("Data Fetched", service.getHikingPoint(id)));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("hiking-points/{id}")
    public ResponseEntity<?> updateHikingPoint(@PathVariable String id, @RequestBody HikingPointRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>("Data Updated", service.updateHikingPoint(id, request)));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("hiking-points/{id}")
    public ResponseEntity<?> deleteHikingPoint(@PathVariable String id) {
        service.deleteHikingPoint(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>("Data Deleted", null));
    }
}
