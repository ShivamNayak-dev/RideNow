package com.ridenow.rider_service.controller;

import com.ridenow.rider_service.dto.CreateRiderRequest;
import com.ridenow.rider_service.dto.UpdateRiderRequest;
import com.ridenow.rider_service.entity.Rider;
import com.ridenow.rider_service.service.RiderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public Rider createRider(
            @RequestParam String userId,
            @Valid @RequestBody CreateRiderRequest request) {

        return riderService.createRider(userId, request);
    }

    @GetMapping("/{userId}")
    public Rider getRider(@PathVariable String userId) {
        return riderService.getRider(userId);
    }

    @PutMapping("/{userId}")
    public Rider updateRider(
            @PathVariable String userId,
            @Valid @RequestBody UpdateRiderRequest request) {

        return riderService.updateRider(userId, request);
    }
}