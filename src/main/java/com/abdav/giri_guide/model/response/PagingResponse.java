package com.abdav.giri_guide.model.response;

public record PagingResponse(
        Integer page,
        Integer size,
        Integer totalPages,
        Long totalElements

) {

}
