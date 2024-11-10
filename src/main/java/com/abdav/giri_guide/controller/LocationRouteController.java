package com.abdav.giri_guide.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.constant.PathApi;
import com.abdav.giri_guide.model.request.LocationRouteRequest;
import com.abdav.giri_guide.model.request.LocationRouteUpdateRequest;
import com.abdav.giri_guide.model.response.CommonResponse;
import com.abdav.giri_guide.service.LocationRouteService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(PathApi.LOCATION_ROUTE_API)
@RequiredArgsConstructor
public class LocationRouteController {
    private final LocationRouteService locationRouteService;

    @GetMapping("")
    public ResponseEntity<?> getAllLocationRoute(
            @RequestParam(required = false, defaultValue = "") String title,
            @RequestParam(required = false, defaultValue = "5") Integer size,
            @RequestParam(required = false, defaultValue = "1") Integer page

    ) {

        return ResponseEntity.status(HttpStatus.OK).body(locationRouteService.getAllRoute(title, page, size));
    }

    @GetMapping("{locationRouteId}")
    public ResponseEntity<?> getLocationRouteDetail(@PathVariable String locationRouteId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(
                        Message.SUCCESS_FETCH, locationRouteService.getRouteDetail(locationRouteId)));
    }

    @PostMapping("")
    public ResponseEntity<?> createLocationRoute(
            @RequestBody @Validated LocationRouteRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(Message.DATA_CREATED, locationRouteService.createRoute(request)));
    }

    @PatchMapping("{locationRouteId}")
    public ResponseEntity<?> updateLocationRoute(
            @PathVariable String locationRouteId,
            @RequestBody @Validated LocationRouteUpdateRequest request

    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(
                        locationRouteId,
                        locationRouteService.updateRoute(locationRouteId, request)));
    }

}
