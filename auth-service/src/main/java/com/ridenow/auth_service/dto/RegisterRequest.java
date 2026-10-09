package com.ridenow.auth_service.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank @Email @Size(max = 254)
    private String email;
    @NotBlank @Size(min = 8, max = 72)
    private String password;
    @NotBlank @Pattern(regexp = "(?i)RIDER|DRIVER", message = "Role must be RIDER or DRIVER")
    private String role;
}
