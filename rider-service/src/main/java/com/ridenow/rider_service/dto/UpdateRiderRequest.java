package com.ridenow.rider_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateRiderRequest(
        @NotBlank
        @Size(max = 100)
        String name,

        @NotBlank
        
        String phone,

        @NotBlank
        @Email
        @Size(max = 150)
        String email
) {}