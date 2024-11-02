package com.abdav.giri_guide.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import com.abdav.giri_guide.model.request.TourGuideRequest;
import com.abdav.giri_guide.model.response.CommonResponse;
import com.abdav.giri_guide.service.TourGuideService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(PathApi.TOUR_GUIDE_API)
@RequiredArgsConstructor
public class TourGuideController {
    private final TourGuideService tourGuideService;

    @PostMapping("")
    @Validated
    public ResponseEntity<?> registerTourGuide(
            @RequestParam MultipartFile image,
            @RequestParam String userId,
            @RequestParam String name,
            @RequestParam String nik,
            @RequestParam String description,

            @RequestParam Integer maxHiker,
            @RequestParam Double price,
            @RequestParam Double additionalPrice,

            @RequestParam Integer totalPorter,
            @RequestParam Double pricePorter

    ) {
        TourGuideRequest request = new TourGuideRequest(
                userId,
                name,
                nik,
                description,
                maxHiker,
                price,
                additionalPrice,
                totalPorter,
                pricePorter);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(
                        Message.DATA_CREATED,
                        tourGuideService.createTourGuide(image, request)));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getTourGuideById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(
                        Message.SUCCESS_FETCH,
                        tourGuideService.getTourGuide(id)));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateTourGuide(@PathVariable String id, @RequestBody TourGuideRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(
                        Message.DATA_UPDATED,
                        tourGuideService.updateTourGuide(id, request)));
    }

    @PatchMapping("{id}/toggle-active")
    public ResponseEntity<?> toggleTourGuideIsActive(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(
                        Message.DATA_UPDATED,
                        tourGuideService.toggleTourGuideActiveStatus(id)));
    }

    @PatchMapping(path = "{id}/update-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateTourGuideImage(@PathVariable String id, @RequestParam MultipartFile image) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(
                        Message.DATA_UPDATED,
                        tourGuideService.updateTourGuideImage(id, image)));
    }

}
