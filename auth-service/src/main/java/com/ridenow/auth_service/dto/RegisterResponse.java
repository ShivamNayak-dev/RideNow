package com.ridenow.auth_service.dto;

public class RegisterResponse {

    private Long id;
    private String email;
    private String role;
    private String status;

    public RegisterResponse(Long id, String email, String role, String status) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }
}