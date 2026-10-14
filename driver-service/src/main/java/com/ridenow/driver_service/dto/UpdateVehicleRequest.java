package com.ridenow.driver_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateVehicleRequest(

        @NotBlank
        @Size(max = 20)
        String vehicleNumber,

        @NotBlank
        @Size(max = 50)
        String type,

        @NotBlank
        @Size(max = 100)
        String model,

        @NotBlank
        @Size(max = 30)
        String vehicleColor
) {}