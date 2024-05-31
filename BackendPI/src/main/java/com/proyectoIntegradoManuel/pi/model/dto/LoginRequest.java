package com.proyectoIntegradoManuel.pi.model.dto;

// DTO para la solicitud de inicio de sesión de usuarios
public class LoginRequest {
    private String email;
    private String password;
    private String role;

    // Métodos getter y setter para cada campo
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}