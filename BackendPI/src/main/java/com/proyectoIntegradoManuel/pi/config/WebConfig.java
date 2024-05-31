package com.proyectoIntegradoManuel.pi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Anotación que define la clase como una clase de configuración
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Método para configurar las políticas de CORS
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Configurar los mapeos de CORS
        registry.addMapping("/api/**") // Define el patrón de URL para el cual se aplican las políticas de CORS
                .allowedOrigins("http://localhost:4200") // Permite solicitudes desde el origen especificado
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite los métodos HTTP especificados
                .allowedHeaders("*") // Permite todos los encabezados
                .allowCredentials(true); // Permite el uso de credenciales (cookies, encabezados de autenticación, etc.)
    }
}