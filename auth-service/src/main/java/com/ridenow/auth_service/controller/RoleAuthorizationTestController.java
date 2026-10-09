package com.ridenow.auth_service.controller;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Temporary endpoints for verifying role-based JWT authorization. */
@RestController
@RequestMapping("/api/auth/test")
public class RoleAuthorizationTestController {

    @GetMapping("/rider")
    @PreAuthorize("hasRole('RIDER')")
    public Map<String, String> rider(@AuthenticationPrincipal Jwt jwt) {
        return Map.of("message", "Rider access granted", "userId", jwt.getSubject(),
                "role", jwt.getClaimAsString("role"));
    }

    @GetMapping("/driver")
    @PreAuthorize("hasRole('DRIVER')")
    public Map<String, String> driver(@AuthenticationPrincipal Jwt jwt) {
        return Map.of("message", "Driver access granted", "userId", jwt.getSubject(),
                "role", jwt.getClaimAsString("role"));
    }
}
