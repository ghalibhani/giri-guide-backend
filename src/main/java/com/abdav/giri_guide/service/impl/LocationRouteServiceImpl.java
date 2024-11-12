package com.abdav.giri_guide.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abdav.giri_guide.constant.Message;
import com.abdav.giri_guide.entity.LocationRoute;
import com.abdav.giri_guide.entity.LocationRouteNode;
import com.abdav.giri_guide.mapper.LocationRouteMapper;
import com.abdav.giri_guide.model.request.LocationRouteRequest;
import com.abdav.giri_guide.model.request.LocationRouteUpdateRequest;
import com.abdav.giri_guide.model.response.CommonResponseWithPage;
import com.abdav.giri_guide.model.response.LocationRouteDetailResponse;
import com.abdav.giri_guide.model.response.LocationRouteListResponse;
import com.abdav.giri_guide.model.response.PagingResponse;
import com.abdav.giri_guide.repository.LocationRouteNodeRepository;
import com.abdav.giri_guide.repository.LocationRouteRepository;
import com.abdav.giri_guide.service.LocationRouteService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationRouteServiceImpl implements LocationRouteService {
    private final LocationRouteRepository routeRepository;
    private final LocationRouteNodeRepository routeNodeRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LocationRouteDetailResponse createRoute(LocationRouteRequest request) {
        LocationRouteNode nextNode = null;
        for (int i = request.routes().size() - 1; i >= 0; i--) {
            LocationRouteNode currentNode = LocationRouteNode.builder()
                    .from(request.routes().get(i).from())
                    .to(request.routes().get(i).to())
                    .estimate(request.routes().get(i).estimate())
                    .transportation(request.routes().get(i).transportation())
                    .distance(request.routes().get(i).distance())
                    .next(nextNode)
                    .build();
            currentNode = routeNodeRepository.saveAndFlush(currentNode);
            nextNode = currentNode;
        }

        LocationRoute route = LocationRoute.builder()
                .title(request.title())
                .description(request.description())
                .startPoint(nextNode)
                .build();
        route = routeRepository.save(route);

        return LocationRouteMapper.toLocationRouteDetailResponse(route);
    }

    @Override
    public void deleteRoute(String locationRouteId) {
        LocationRoute route = routeRepository.findById(locationRouteId).orElseThrow(
                () -> new EntityNotFoundException("Route " + Message.DATA_NOT_FOUND));

        deleteNodeRecusive(route);
        route.setDeletedDate(LocalDateTime.now());
        routeRepository.save(route);

    }

    @Override
    public CommonResponseWithPage<List<LocationRouteListResponse>> getAllRoute(
            String title, Integer page, Integer size) {

        if (page <= 0) {
            page = 1;
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<LocationRoute> routesPage = routeRepository
                .findByTitleContainingIgnoreCaseAndDeletedDateIsNull(title, pageable);

        PagingResponse paging = new PagingResponse(
                page,
                size,
                routesPage.getTotalPages(),
                routesPage.getTotalElements());

        List<LocationRouteListResponse> routeList = LocationRouteMapper
                .toListOfLocationListResponse(routesPage.getContent());

        return new CommonResponseWithPage<>(Message.SUCCESS_FETCH, routeList, paging);
    }

    @Override
    public LocationRouteDetailResponse getRouteDetail(String locationRouteId) {
        LocationRoute route = routeRepository.findById(locationRouteId).orElseThrow(
                () -> new EntityNotFoundException("Route " + Message.DATA_NOT_FOUND));

        return LocationRouteMapper.toLocationRouteDetailResponse(route);
    }

    @Override
    public LocationRouteDetailResponse updateRoute(String locationRouteId, LocationRouteUpdateRequest request) {
        LocationRoute route = routeRepository.findById(locationRouteId).orElseThrow(
                () -> new EntityNotFoundException("Route " + Message.DATA_NOT_FOUND));

        if (request.title() != null) {
            route.setTitle(request.title());
        }
        if (request.description() != null) {
            route.setDescription(request.description());
        }
        if (request.routes() != null || !request.routes().isEmpty()) {
            LocationRouteNode nextNode = null;
            for (int i = request.routes().size() - 1; i >= 0; i--) {
                LocationRouteNode currentNode = LocationRouteNode.builder()
                        .from(request.routes().get(i).from())
                        .to(request.routes().get(i).to())
                        .estimate(request.routes().get(i).estimate())
                        .transportation(request.routes().get(i).transportation())
                        .distance(request.routes().get(i).distance())
                        .next(nextNode)
                        .build();
                currentNode = routeNodeRepository.saveAndFlush(currentNode);
                nextNode = currentNode;
            }

            deleteNodeRecusive(route);
            route.setStartPoint(nextNode);
        }
        route = routeRepository.save(route);
        return LocationRouteMapper.toLocationRouteDetailResponse(route);
    }

    private void deleteNodeRecusive(LocationRoute route) {
        LocationRouteNode currentNode = route.getStartPoint();
        if (currentNode == null) {
            return;
        }

        LocationRouteNode nextNode = currentNode.getNext();

        route.setStartPoint(null);
        routeRepository.saveAndFlush(route);

        List<LocationRouteNode> deletedNode = new ArrayList<>();
        while (nextNode != null) {
            currentNode.setNext(null);
            deletedNode.add(currentNode);

            currentNode = nextNode;
            nextNode = currentNode.getNext();
        }
        routeNodeRepository.saveAllAndFlush(deletedNode);
        routeNodeRepository.deleteAll(deletedNode);
    }

}
