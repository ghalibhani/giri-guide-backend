package com.abdav.giri_guide.model.response;

public record CommonResponse<T>(
        String message,
        T data) {
}
