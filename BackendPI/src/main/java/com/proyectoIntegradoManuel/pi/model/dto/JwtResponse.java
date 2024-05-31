package com.proyectoIntegradoManuel.pi.model.dto;

import com.proyectoIntegradoManuel.pi.model.Usuario;

// DTO para la respuesta de autenticación JWT
public class JwtResponse {
    private String token;
    private Usuario usuario;

    // Constructor con parámetros
    public JwtResponse(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    // Métodos getter y setter para cada campo

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}