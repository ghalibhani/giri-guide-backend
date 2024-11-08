package com.abdav.giri_guide.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import com.abdav.giri_guide.constant.EGender;
import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.constant.PathApi;
import com.abdav.giri_guide.model.request.TourGuideAddHikingPointRequest;
import com.abdav.giri_guide.model.request.TourGuideRequest;
import com.abdav.giri_guide.model.request.UserIdRequest;
import com.abdav.giri_guide.model.response.CommonResponse;
import com.abdav.giri_guide.service.TourGuideService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(PathApi.TOUR_GUIDE_API)
@RequiredArgsConstructor
public class TourGuideController {
    private final TourGuideService tourGuideService;

    @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Validated
    public ResponseEntity<?> registerTourGuide(
            @RequestParam MultipartFile image,

            @RequestParam String email,
            @RequestParam String password,

            @RequestParam String name,
            @RequestParam EGender gender,
            @RequestParam String nik,
            @RequestParam String birthDate,
            @RequestParam String description,
            @RequestParam String address,

            @RequestParam Integer maxHiker,
            @RequestParam Long price,
            @RequestParam Long additionalPrice,

            @RequestParam Integer totalPorter,
            @RequestParam Long pricePorter,
            HttpServletRequest httpReq

    ) {
        TourGuideRequest request = new TourGuideRequest(
                email,
                password,
                name,
                gender,
                nik,
                Date.from(LocalDate.parse(birthDate).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                description,
                address,
                maxHiker,
                price,
                additionalPrice,
                totalPorter,
                pricePorter);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(
                        Message.DATA_CREATED,
                        tourGuideService.createTourGuide(image, request, httpReq)));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getTourGuideById(@PathVariable String id, HttpServletRequest httpReq) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(
                        Message.SUCCESS_FETCH,
                        tourGuideService.getTourGuide(id, httpReq)));
    }

    @PatchMapping("{userId}/toggle-active")
    public ResponseEntity<?> toggleTourGuideIsActive(@PathVariable String userId, HttpServletRequest httpReq) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(
                        Message.DATA_UPDATED,
                        tourGuideService.toggleTourGuideActiveStatus(userId, httpReq)));
    }

    @GetMapping("profile/{userId}")
    public ResponseEntity<?> getTourGuideProfileByUserId(
            @PathVariable String userId,
            HttpServletRequest httpReq) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(tourGuideService.getTourGuideProfile(userId, httpReq));
    }

    @GetMapping("data/{id}")
    public ResponseEntity<?> getTourGuideData(
            @PathVariable String id,
            HttpServletRequest httpReq) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(tourGuideService.getTourGuideData(id, httpReq));
    }

    @PatchMapping("data/{id}")
    public ResponseEntity<?> updateTourGuide(
            @PathVariable String id,
            @RequestBody TourGuideRequest request,
            HttpServletRequest httpReq) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(
                        Message.DATA_UPDATED,
                        tourGuideService.updateTourGuide(id, request, httpReq)));
    }

    @PatchMapping(path = "data/{id}/update-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateTourGuideImage(
            @PathVariable String id,
            @RequestParam MultipartFile image,
            HttpServletRequest httpReq) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(
                        Message.DATA_UPDATED,
                        tourGuideService.updateTourGuideImage(id, image, httpReq)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTourGuide(@PathVariable String id) {
        tourGuideService.softDeleteTourGuide(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(
                        Message.SUCCESS_DELETE,
                        null));
    }

    @PostMapping("{id}/hiking-points")
    public ResponseEntity<?> addTourGuideHikingPoint(
            @PathVariable String id,
            @RequestBody TourGuideAddHikingPointRequest request,
            HttpServletRequest httpReq

    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(
                        Message.DATA_UPDATED,
                        tourGuideService.addHikingPoint(id, request, httpReq)));
    }

    @DeleteMapping("{tourGuideId}/hiking-points/{hikingPointId}")
    public ResponseEntity<?> deleteTourGuideHikingPoint(
            @PathVariable String tourGuideId,
            @PathVariable String hikingPointId) {

        tourGuideService.softDeleteTourGuideHikingPoint(tourGuideId, hikingPointId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(Message.SUCCESS_DELETE, null));
    }

    @GetMapping("")
    public ResponseEntity<?> getTourGuideList(
            @RequestParam(required = false, defaultValue = "") String hikingPoint,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            HttpServletRequest httpReq

    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(tourGuideService.getTourGuideList(hikingPoint, size, page, httpReq));
    }

    @GetMapping("profile/{userId}/hiking-points")
    public ResponseEntity<?> getProfileHikingPoints(@PathVariable String userId) {
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<>(
                Message.SUCCESS_FETCH,
                tourGuideService.getTourGuideHikingPointActiveList(userId)));
    }

    @PatchMapping("profile/{userId}/hiking-points/{tourGuideHikingPointId}/toggle")
    public ResponseEntity<?> toggleProfileHikingPoints(
            @PathVariable String userId,
            @PathVariable String tourGuideHikingPointId) {

        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<>(
                Message.DATA_UPDATED,
                tourGuideService.toggleTourGuideHikingPointActiveList(userId, tourGuideHikingPointId)));

    }

    @GetMapping("stats/{userId}")
    public ResponseEntity<?> getTourGuideStats(@PathVariable String userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(Message.SUCCESS_FETCH,
                        tourGuideService.getTourGuideStats(userId)));
    }

}
