package com.proyectoIntegradoManuel.pi.controller;

import com.proyectoIntegradoManuel.pi.model.Consumidor;
import com.proyectoIntegradoManuel.pi.model.Ofertante;
import com.proyectoIntegradoManuel.pi.services.OfertanteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/ofertantes")
// Permite solicitudes CORS desde el origen especificado
@CrossOrigin(origins = "http://localhost:4200")
public class OfertanteController {

    // Inyección de dependencias
    @Autowired
    private OfertanteService ofertanteService;

    // Logger para registrar información, advertencias y errores en el controlador
    static final Logger logger = LoggerFactory.getLogger(OfertanteController.class);

    // Endpoint para obtener un ofertante por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Ofertante> obtenerOfertantePorId(@PathVariable Long id) {
        logger.info("Obteniendo ofertante con id: {}", id);

        // Buscar el ofertante por su ID utilizando el servicio
        Ofertante ofertante = ofertanteService.findById(id);
        if (ofertante != null) {
            return ResponseEntity.ok(ofertante);
        } else {
            logger.warn("Ofertante no encontrado con id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Endpoint para editar un ofertante existente
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarOfertante(@PathVariable Long id, @RequestBody Ofertante ofertanteDetails) {
        logger.info("Intentando actualizar el ofertante con id: {}", id);

        // Actualizar el ofertante utilizando el servicio
        Ofertante updatedOfertante = ofertanteService.updateOfertante(ofertanteDetails);
        if (updatedOfertante != null) {
            logger.info("Ofertante actualizado correctamente: {}", updatedOfertante);
            return ResponseEntity.ok(updatedOfertante);
        } else {
            logger.warn("No se encontró el ofertante con id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ofertante no encontrado");
        }
    }

    // Endpoint para eliminar un ofertante por su ID
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarOfertante(@PathVariable Long id) {
        logger.info("Intentando eliminar ofertante con id: {}", id);

        // Buscar el ofertante por su ID utilizando el servicio
        Ofertante ofertante = ofertanteService.findById(id);
        if (ofertante != null) {
            try {
                ofertanteService.deleteById(id);
                logger.info("Ofertante eliminado con id: {}", id);
                return ResponseEntity.ok(Collections.singletonMap("message", "Ofertante eliminado exitosamente"));
            } catch (DataIntegrityViolationException e) {
                logger.error("Error al eliminar ofertante debido a relaciones con otras entidades:", e);
                return ResponseEntity.status(HttpStatus.CONFLICT).body("No se puede eliminar el ofertante porque tiene actividades asociadas.");
            }
        } else {
            logger.warn("Ofertante no encontrado con id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ofertante no encontrado");
        }
    }
}