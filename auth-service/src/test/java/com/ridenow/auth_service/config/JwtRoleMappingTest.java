package com.ridenow.auth_service.config;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

class JwtRoleMappingTest {
    private final SecurityConfig securityConfig = new SecurityConfig();

    @Test
    void riderRoleClaimMapsToRiderAuthority() {
        JwtAuthenticationToken authentication = convertRole("RIDER");
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_RIDER")));
    }

    @Test
    void driverRoleClaimMapsToDriverAuthority() {
        JwtAuthenticationToken authentication = convertRole("DRIVER");
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_DRIVER")));
    }

    private JwtAuthenticationToken convertRole(String role) {
        Jwt jwt = Jwt.withTokenValue("test-token")
                .header("alg", "HS256")
                .subject("17")
                .claim("role", role)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(900))
                .build();
        return (JwtAuthenticationToken) securityConfig.jwtAuthenticationConverter().convert(jwt);
    }
}
