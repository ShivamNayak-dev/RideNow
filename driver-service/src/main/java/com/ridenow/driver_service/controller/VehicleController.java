
package com.ridenow.driver_service.controller;

import com.ridenow.driver_service.dto.CreateVehicleRequest;
import com.ridenow.driver_service.dto.UpdateVehicleRequest;
import com.ridenow.driver_service.dto.VehicleResponse;
import com.ridenow.driver_service.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drivers/me/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<VehicleResponse> createMyVehicle(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateVehicleRequest request) {

        VehicleResponse response = vehicleService.createMyVehicle(
                jwt.getSubject(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<VehicleResponse> getMyVehicle(
            @AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok(
                vehicleService.getMyVehicle(jwt.getSubject()));
    }

    @PutMapping
    public ResponseEntity<VehicleResponse> updateMyVehicle(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateVehicleRequest request) {

        return ResponseEntity.ok(
                vehicleService.updateMyVehicle(
                        jwt.getSubject(), request));
    }
}
