package com.abdav.giri_guide.model.response;

public record CommonResponseWithPage<T>(
        String message,
        T data,
        PagingResponse paging) {
}
