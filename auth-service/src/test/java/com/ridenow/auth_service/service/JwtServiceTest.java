package com.ridenow.auth_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Base64;

import com.ridenow.auth_service.config.JwtConfig;
import com.ridenow.auth_service.entity.Role;
import com.ridenow.auth_service.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

class JwtServiceTest {
    @Test
    void generatedJwtHasExpectedIssuerIdentityRoleAndLifetime() {
        String secret = Base64.getEncoder().encodeToString(new byte[32]);
        JwtConfig config = new JwtConfig();
        JwtEncoder encoder = config.jwtEncoder(secret);
        JwtDecoder decoder = config.jwtDecoder(secret);
        JwtService service = new JwtService(encoder, 15);
        User user = new User();
        user.setId(42L);
        user.setEmail("driver@example.com");
        user.setRole(Role.DRIVER);

        Jwt decoded = decoder.decode(service.generateToken(user));

        assertEquals("ridenow-auth-service", decoded.getClaimAsString("iss"));
        assertEquals("42", decoded.getSubject());
        assertEquals("driver@example.com", decoded.getClaimAsString("email"));
        assertEquals("DRIVER", decoded.getClaimAsString("role"));
        assertEquals(900, decoded.getExpiresAt().getEpochSecond() - decoded.getIssuedAt().getEpochSecond());
    }
}
