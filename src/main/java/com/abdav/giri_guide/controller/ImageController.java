package com.abdav.giri_guide.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.abdav.giri_guide.entity.ImageEntity;
import com.abdav.giri_guide.model.response.CommonResponse;
import com.abdav.giri_guide.model.response.ImageUploadResponse;
import com.abdav.giri_guide.service.ImageService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse<ImageUploadResponse>> create(
            @RequestParam MultipartFile image, HttpServletRequest request

    ) {
        ImageEntity imageEntity = imageService.create(image, "/images", "kokoronTest");

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>("Image Uploaded", new ImageUploadResponse(
                        imageEntity.getId(),
                        imageEntity.getPath(),
                        imageEntity.getMediaType())));
    }
}
