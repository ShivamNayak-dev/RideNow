package com.ridenow.auth_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.ridenow.auth_service.dto.LoginRequest;
import com.ridenow.auth_service.dto.LoginResponse;
import com.ridenow.auth_service.dto.RegisterRequest;
import com.ridenow.auth_service.dto.RegisterResponse;
import com.ridenow.auth_service.entity.Role;
import com.ridenow.auth_service.entity.User;
import com.ridenow.auth_service.entity.UserStatus;
import com.ridenow.auth_service.exception.AccountNotActiveException;
import com.ridenow.auth_service.exception.EmailAlreadyRegisteredException;
import com.ridenow.auth_service.exception.InvalidCredentialsException;
import com.ridenow.auth_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class AuthServiceTest {
    private UserRepository repository;
    private JwtService jwtService;
    private BCryptPasswordEncoder passwordEncoder;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);
        jwtService = mock(JwtService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthService(repository, jwtService, passwordEncoder);
    }

    @Test
    void registersNormalizedEmailAndHashedPassword() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("  Rider@Example.com ");
        request.setPassword("correct-horse-1");
        request.setRole("rider");
        when(repository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(9L);
            user.setStatus(UserStatus.ACTIVE);
            return user;
        });

        RegisterResponse response = authService.register(request);

        assertEquals("rider@example.com", response.getEmail());
        verify(repository).existsByEmail("rider@example.com");
        verify(repository).save(argThat(user -> user.getEmail().equals("rider@example.com")
                && !user.getPasswordHash().equals("correct-horse-1")
                && passwordEncoder.matches("correct-horse-1", user.getPasswordHash())
                && user.getRole() == Role.RIDER));
    }

    @Test
    void rejectsDuplicateRegistration() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("Rider@example.com");
        request.setPassword("correct-horse-1");
        request.setRole("RIDER");
        when(repository.existsByEmail("rider@example.com")).thenReturn(true);
        assertThrows(EmailAlreadyRegisteredException.class, () -> authService.register(request));
        verify(repository, never()).save(any());
    }

    @Test
    void loginReturnsTokenAndFifteenMinuteExpiry() {
        User user = user("rider@example.com", "correct-horse-1", UserStatus.ACTIVE);
        when(repository.findByEmail("rider@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("token");
        when(jwtService.getExpirationSeconds()).thenReturn(900L);
        LoginRequest request = new LoginRequest();
        request.setEmail(" RIDER@EXAMPLE.COM ");
        request.setPassword("correct-horse-1");

        LoginResponse response = authService.login(request);

        assertEquals("token", response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(900L, response.getExpiresIn());
        verify(repository).findByEmail("rider@example.com");
    }

    @Test
    void unknownEmailAndWrongPasswordHaveSameFailure() {
        LoginRequest request = new LoginRequest();
        request.setEmail("missing@example.com");
        request.setPassword("correct-horse-1");
        when(repository.findByEmail(any())).thenReturn(Optional.empty());
        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));

        when(repository.findByEmail(any())).thenReturn(Optional.of(
                user("missing@example.com", "correct-horse-1", UserStatus.ACTIVE)));
        request.setPassword("wrong-password");
        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void inactiveAccountCannotReceiveToken() {
        when(repository.findByEmail("rider@example.com")).thenReturn(Optional.of(
                user("rider@example.com", "correct-horse-1", UserStatus.BLOCKED)));
        LoginRequest request = new LoginRequest();
        request.setEmail("rider@example.com");
        request.setPassword("correct-horse-1");
        assertThrows(AccountNotActiveException.class, () -> authService.login(request));
        verify(jwtService, never()).generateToken(any());
    }

    private User user(String email, String password, UserStatus status) {
        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(Role.RIDER);
        user.setStatus(status);
        return user;
    }
}
