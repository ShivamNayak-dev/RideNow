package com.ridenow.auth_service.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class LoginRequest {

    @NotBlank @Email @Size(max = 254)
    private String email;
    @NotBlank
    private String password;
}
