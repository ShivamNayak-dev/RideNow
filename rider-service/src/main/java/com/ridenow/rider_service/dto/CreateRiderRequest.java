package com.ridenow.rider_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateRiderRequest(
        @NotBlank
        @Size(max = 100)
        String name,

        @NotBlank
        @Pattern(
                regexp = "^[0-9+() -]{7,20}$",
                message = "Phone must contain 7 to 20 characters using digits or + ( ) -"
        )
        String phone,

        @NotBlank
        @Email
        @Size(max = 150)
        String email
) {}