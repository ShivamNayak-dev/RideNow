package com.ridenow.auth_service.controller;

import com.ridenow.auth_service.dto.RegisterRequest;
import com.ridenow.auth_service.dto.RegisterResponse;
import com.ridenow.auth_service.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.ridenow.auth_service.dto.LoginRequest;
import com.ridenow.auth_service.dto.LoginResponse;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}