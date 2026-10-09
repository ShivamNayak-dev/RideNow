package com.ridenow.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private Long id;
    private String email;
    private String role;
    private String status;

    private String accessToken;
    private String tokenType;
    private long expiresIn;
}
