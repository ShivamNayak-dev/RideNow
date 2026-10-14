package com.ridenow.driver_service.dto;

import com.ridenow.driver_service.enums.DriverStatus;
import java.time.LocalDateTime;

public record DriverResponse(
        Long id,
        String userId,
        String name,
        String phone,
        String licenseNumber,
        DriverStatus status,
        Double rating,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}