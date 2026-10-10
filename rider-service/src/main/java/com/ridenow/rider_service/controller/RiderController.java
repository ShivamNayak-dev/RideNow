package com.ridenow.rider_service.controller;

import com.ridenow.rider_service.dto.CreateRiderRequest;
import com.ridenow.rider_service.dto.RiderResponse;
import com.ridenow.rider_service.dto.UpdateRiderRequest;
import com.ridenow.rider_service.entity.Rider;
import com.ridenow.rider_service.service.RiderService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/riders")
public class RiderController {

    private final RiderService riderService;

    public RiderController(RiderService riderService) {
        this.riderService = riderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RiderResponse createRider(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateRiderRequest request) {

        Rider rider = riderService.createRider(
                jwt.getSubject(), request);

        return toResponse(rider);
    }

    @GetMapping("/me")
    public RiderResponse getMyProfile(
            @AuthenticationPrincipal Jwt jwt) {

        return toResponse(
                riderService.getRider(jwt.getSubject()));
    }

    @PutMapping("/me")
    public RiderResponse updateMyProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateRiderRequest request) {

        return toResponse(
                riderService.updateRider(
                        jwt.getSubject(), request));
    }

    private RiderResponse toResponse(Rider rider) {
        return new RiderResponse(
                rider.getId(),
                rider.getName(),
                rider.getPhone(),
                rider.getEmail(),
                rider.getCreatedAt(),
                rider.getUpdatedAt());
    }
}