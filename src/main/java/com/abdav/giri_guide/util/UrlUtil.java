package com.abdav.giri_guide.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.abdav.giri_guide.entity.ImageEntity;

import jakarta.servlet.http.HttpServletRequest;

public class UrlUtil {
    private UrlUtil() {
    }

    public static String resolveImageUrl(ImageEntity imageEntity, HttpServletRequest request) {

        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();

        return String.format("%s%s", baseUrl, imageEntity.getPath());
    }
}
