package com.ridenow.driver_service.dto;

import java.time.LocalDateTime;

public record VehicleResponse(
        Long id,
        Long driverId,
        String vehicleNumber,
        String type,
        String model,
        String vehicleColor,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}