package com.proyectoIntegradoManuel.pi.controller;

import com.proyectoIntegradoManuel.pi.model.Actividad;
import com.proyectoIntegradoManuel.pi.model.Consumidor;
import com.proyectoIntegradoManuel.pi.services.ConsumidorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/consumidores")
// Permite solicitudes CORS desde el origen especificado
@CrossOrigin(origins = "http://localhost:4200")
public class ConsumidorController {

    // Inyección de dependencias
    @Autowired
    private ConsumidorService consumidorService;

    // Logger para registrar información, advertencias y errores en el controlador
    static final Logger logger = LoggerFactory.getLogger(ConsumidorController.class);

    // Endpoint para obtener un consumidor por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Consumidor> getConsumidorById(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para obtener consumidor con id: {}", id);

        // Buscar el consumidor por su ID utilizando el servicio
        Consumidor consumidor = consumidorService.findById(id);
        if (consumidor != null) {
            logger.info("Consumidor encontrado: {}", consumidor);
            return ResponseEntity.ok(consumidor);
        } else {
            logger.warn("Consumidor con id {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para editar un consumidor existente
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarConsumidor(@PathVariable Long id, @RequestBody Consumidor consumidorDetails) {
        logger.info("Intentando actualizar el consumidor con id: {}", id);

        // Actualizar el consumidor utilizando el servicio
        Consumidor updatedConsumidor = consumidorService.updateConsumidor(consumidorDetails);
        if (updatedConsumidor != null) {
            logger.info("Consumidor actualizado correctamente: {}", updatedConsumidor);
            return ResponseEntity.ok(updatedConsumidor);
        } else {
            logger.warn("No se encontró el consumidor con id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Consumidor no encontrado");
        }
    }

    // Endpoint para eliminar un consumidor por su ID
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarConsumidor(@PathVariable Long id) {
        logger.info("Intentando eliminar consumidor con id: {}", id);

        // Buscar el consumidor por su ID utilizando el servicio
        Consumidor consumidor = consumidorService.findById(id);
        if (consumidor != null) {
            // Eliminar el consumidor utilizando el servicio
            consumidorService.deleteById(consumidor.getId());
            logger.info("Consumidor eliminado con id: {}", id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Consumidor eliminado exitosamente"));
        } else {
            logger.warn("Consumidor no encontrado con id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Consumidor no encontrado"));
        }
    }
}