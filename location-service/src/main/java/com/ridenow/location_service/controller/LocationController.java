
package com.ridenow.location_service.controller;

import com.ridenow.location_service.dto.LocationResponse;
import com.ridenow.location_service.dto.LocationUpdateRequest;
import com.ridenow.location_service.service.LocationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PutMapping("/me")
    public ResponseEntity<LocationResponse> updateLocation(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody LocationUpdateRequest request) {

        String driverId = jwt.getSubject();

        LocationResponse response =
                locationService.updateLocation(driverId, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<LocationResponse> getLocation(
            @AuthenticationPrincipal Jwt jwt) {

        String driverId = jwt.getSubject();

        LocationResponse response = locationService
                .getLocation(driverId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Location not found or expired"
                ));

        return ResponseEntity.ok(response);
    }
}
