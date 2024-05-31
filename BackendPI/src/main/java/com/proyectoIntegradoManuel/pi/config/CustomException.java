package com.proyectoIntegradoManuel.pi.config;

import org.springframework.http.HttpStatus;

// Clase que extiende de RuntimeException para manejar excepciones personalizadas
public class CustomException extends RuntimeException {
    // Campo que almacena el estado HTTP asociado con la excepción
    private HttpStatus status;

    // Constructor que inicializa el mensaje de la excepción y el estado HTTP
    public CustomException(String message, HttpStatus status) {
        // Llamada al constructor de la clase padre (RuntimeException) con el mensaje
        super(message);
        // Inicialización del estado HTTP
        this.status = status;
    }

    // Método getter para obtener el estado HTTP
    public HttpStatus getStatus() {
        return status;
    }
}