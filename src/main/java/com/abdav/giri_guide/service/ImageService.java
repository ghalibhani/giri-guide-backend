package com.abdav.giri_guide.service;

import org.springframework.web.multipart.MultipartFile;

import com.abdav.giri_guide.entity.ImageEntity;

public interface ImageService {
    ImageEntity create(MultipartFile multipartFile, String location, String name);

    ImageEntity getById();

    ImageEntity getByPath();

    void delete();
}
