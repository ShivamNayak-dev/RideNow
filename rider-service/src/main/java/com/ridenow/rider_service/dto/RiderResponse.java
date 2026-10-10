package com.ridenow.rider_service.dto;

import java.time.LocalDateTime;

public record RiderResponse(
        Long id,
        String name,
        String phone,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}