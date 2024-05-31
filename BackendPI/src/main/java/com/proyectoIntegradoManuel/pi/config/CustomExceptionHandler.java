package com.proyectoIntegradoManuel.pi.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Anotación que define la clase como un controlador de asesoría para excepciones REST
@RestControllerAdvice
public class CustomExceptionHandler {

    // Método para manejar excepciones de tipo CustomException
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        // Crear una respuesta de error con el mensaje de la excepción
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        // Retornar la respuesta de error con el estado HTTP de la excepción
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    // Método para manejar excepciones de tipo ForeignKeyException
    @ExceptionHandler(ForeignKeyException.class)
    public ResponseEntity<ErrorResponse> handleForeignKeyConstraintViolationException(ForeignKeyException ex) {
        // Crear una respuesta de error con el mensaje de la excepción
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        // Retornar la respuesta de error con el estado HTTP de la excepción
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    // Clase estática para representar la respuesta de error
    @Data
    @AllArgsConstructor
    public static class ErrorResponse {
        // Campo para almacenar el mensaje de error
        private String message;
    }
}