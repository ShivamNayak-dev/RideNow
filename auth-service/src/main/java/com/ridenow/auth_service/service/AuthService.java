package com.ridenow.auth_service.service;

import com.ridenow.auth_service.dto.RegisterRequest;
import com.ridenow.auth_service.dto.RegisterResponse;
import com.ridenow.auth_service.entity.Role;
import com.ridenow.auth_service.entity.User;
import com.ridenow.auth_service.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ridenow.auth_service.dto.LoginRequest;
import com.ridenow.auth_service.dto.LoginResponse;
import com.ridenow.auth_service.entity.UserStatus;
import com.ridenow.auth_service.exception.EmailAlreadyRegisteredException;
import com.ridenow.auth_service.exception.InvalidCredentialsException;
import com.ridenow.auth_service.exception.AccountNotActiveException;
import java.util.Locale;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterResponse register(RegisterRequest request) {

        String email = normalizeEmail(request.getEmail());
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyRegisteredException();
        }

        Role role = Role.valueOf(request.getRole().trim().toUpperCase(Locale.ROOT));

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        User saved = userRepository.save(user);

        return new RegisterResponse(
                saved.getId(),
                saved.getEmail(),
                saved.getRole().name(),
                saved.getStatus().name()
        );





    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(normalizeEmail(request.getEmail()))
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new AccountNotActiveException();
        }

        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getRole().name(),
                user.getStatus().name(),
                jwtService.generateToken(user),
                "Bearer",
                jwtService.getExpirationSeconds()
        );
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
