package com.ridenow.driver_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateDriverRequest(
        @NotBlank @Size(max = 100)
        String name,

        @NotBlank
        @Pattern(regexp = "^[0-9+() -]{7,20}$")
        String phone,

        @NotBlank @Size(max = 50)
        String licenseNumber
) {}