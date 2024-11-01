package com.abdav.giri_guide.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.abdav.giri_guide.entity.ImageEntity;
import com.abdav.giri_guide.repository.ImageRepository;
import com.abdav.giri_guide.service.ImageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    private final Path rootPath = Paths.get(System.getenv("IMAGE_LOCATION"));

    @Override
    public ImageEntity create(MultipartFile multipartFile, String location, String name) {
        if (multipartFile.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File not existed");
        }

        try {
            Path directoryPath = Paths.get(rootPath.toString(), location);
            Files.createDirectories(directoryPath);
            String extension = multipartFile.getContentType().split("/")[1];
            String filename = String.format("%d-%s.%s", System.currentTimeMillis(), name, extension);
            Path filePath = directoryPath.resolve(filename);
            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String imagePath = String.format("%s/%s", location, filename);
            return imageRepository.saveAndFlush(ImageEntity.builder()
                    .name(filename)
                    .path(imagePath)
                    .mediaType(multipartFile.getContentType())
                    .size(multipartFile.getSize())
                    .build());

        } catch (IOException e) {
            throw new RuntimeException();

        }
    }

    @Override
    public void delete() {
        // TODO Auto-generated method stub

    }

    @Override
    public ImageEntity getById() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ImageEntity getByPath() {
        // TODO Auto-generated method stub
        return null;
    }

}
