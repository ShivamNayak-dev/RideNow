package com.ridenow.auth_service.service;

import com.ridenow.auth_service.dto.RegisterRequest;
import com.ridenow.auth_service.dto.RegisterResponse;
import com.ridenow.auth_service.entity.Role;
import com.ridenow.auth_service.entity.User;
import com.ridenow.auth_service.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.ridenow.auth_service.dto.LoginRequest;
import com.ridenow.auth_service.dto.LoginResponse;
import com.ridenow.auth_service.entity.UserStatus;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        Role role = Role.valueOf(request.getRole().toUpperCase());
        if (role == Role.ADMIN) {
            throw new IllegalArgumentException("Cannot self-register as ADMIN");
        }

        User user = new User();
        user.setEmail(request.getEmail());
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

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException("User account is not active");
        }

        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getRole().name(),
                user.getStatus().name()
        );
    }
}