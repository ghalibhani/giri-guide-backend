package com.abdav.giri_guide.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abdav.giri_guide.model.request.MountainsRequest;
import com.abdav.giri_guide.model.response.CommonResponse;
import com.abdav.giri_guide.model.response.CommonResponseWithPage;
import com.abdav.giri_guide.model.response.MountainsDetailResponse;
import com.abdav.giri_guide.model.response.MountainsListResponse;
import com.abdav.giri_guide.model.response.PagingResponse;
import com.abdav.giri_guide.service.MountainsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/mountains")
public class MountainController {
    private final MountainsService service;

    @GetMapping("")
    public ResponseEntity<CommonResponseWithPage<?>> getMountainList() {
        Page<MountainsListResponse> mountainList = service.mountainList(null, 1, 5);
        PagingResponse paging = new PagingResponse(1, 5, mountainList.getTotalPages(), mountainList.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponseWithPage<List<MountainsListResponse>>(
                        "Data fetched",
                        mountainList.getContent(),
                        paging));
    }

    @PostMapping("")
    public ResponseEntity<CommonResponse<?>> createMountain(@RequestBody MountainsRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<MountainsDetailResponse>("Mountain data successfully created",
                        service.createMountain(request.toMountains())));
    }

    @GetMapping("{id}")
    public ResponseEntity<CommonResponse<?>> getMountainDetail(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<MountainsDetailResponse>("Data fetched successfully",
                        service.mountainDetail(id)));
    }

    @PutMapping("{id}")
    public ResponseEntity<CommonResponse<?>> updateMountain(
            @PathVariable String id,
            @RequestBody MountainsRequest request) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<MountainsDetailResponse>("Data updated successfully",
                        service.updateMountain(id, request.toMountains())));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CommonResponse<?>> deleteMountain(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse<Object>("Data deleted successfully", null));
    }

}
