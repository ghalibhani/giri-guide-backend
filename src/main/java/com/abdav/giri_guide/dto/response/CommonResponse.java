package com.abdav.giri_guide.dto.response;

public record CommonResponse<T>(String message, T data) {
}
