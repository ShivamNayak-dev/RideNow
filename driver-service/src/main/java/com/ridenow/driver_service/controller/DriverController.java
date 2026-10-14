
package com.ridenow.driver_service.controller;

import com.ridenow.driver_service.dto.CreateDriverRequest;
import com.ridenow.driver_service.dto.DriverResponse;
import com.ridenow.driver_service.dto.UpdateDriverRequest;
import com.ridenow.driver_service.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    public ResponseEntity<DriverResponse> createProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateDriverRequest request) {

        DriverResponse response = driverService.createDriver(
                jwt.getSubject(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<DriverResponse> getMyProfile(
            @AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok(
                driverService.getMyProfile(jwt.getSubject()));
    }

    @PutMapping("/me")
    public ResponseEntity<DriverResponse> updateMyProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateDriverRequest request) {

        return ResponseEntity.ok(
                driverService.updateMyProfile(
                        jwt.getSubject(), request));
    }

    @PutMapping("/me/online")
    public ResponseEntity<DriverResponse> goOnline(
            @AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok(
                driverService.goOnline(jwt.getSubject()));
    }

    @PutMapping("/me/offline")
    public ResponseEntity<DriverResponse> goOffline(
            @AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok(
                driverService.goOffline(jwt.getSubject()));
    }
}
