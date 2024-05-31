package com.proyectoIntegradoManuel.pi.controller;

import com.proyectoIntegradoManuel.pi.model.Reserva;
import com.proyectoIntegradoManuel.pi.services.ReservaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
// Permite solicitudes CORS desde el origen especificado
@CrossOrigin(origins = "http://localhost:4200")
public class ReservaController {
    @Autowired
    private ReservaService reservaService;
    static final Logger logger = LoggerFactory.getLogger(ReservaController.class);

    // Crea una reserva.
    @PostMapping
    public ResponseEntity<Reserva> createReserva(@RequestParam Long actividadId, @RequestParam Long consumidorId) {
        logger.info("Solicitud para crear reserva: actividad ID {}, consumidor ID {}", actividadId, consumidorId);
        Reserva reserva = reservaService.createReserva(actividadId, consumidorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(reserva);
    }

    // Obtienes las reservas de un consumidor en especifico y te las muestra en paginado.
    @GetMapping("/consumidor/{consumidorId}")
    public ResponseEntity<Page<Reserva>> getReservasByConsumidor(
            @PathVariable Long consumidorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        logger.info("Solicitud para obtener reservas del consumidor ID {}", consumidorId);
        Pageable pageable = PageRequest.of(page, size);
        Page<Reserva> reservasPage = reservaService.getReservasByConsumidor(consumidorId, pageable);
        return ResponseEntity.ok(reservasPage);
    }

    // Obtener las reservas de una actividad específica.
    @GetMapping("/actividad/{actividadId}")
    public ResponseEntity<List<Reserva>> getReservasByActividad(@PathVariable Long actividadId) {
        logger.info("Solicitud para obtener reservas de la actividad ID {}", actividadId);
        List<Reserva> reservas = reservaService.getReservasByActividad(actividadId);
        return ResponseEntity.ok(reservas);
    }

    // Cancelar una reserva específica.
    @DeleteMapping("/{reservaId}")
    public ResponseEntity<Void> cancelReserva(@PathVariable Long reservaId) {
        logger.info("Solicitud para cancelar reserva ID {}", reservaId);
        reservaService.cancelReserva(reservaId);
        return ResponseEntity.noContent().build();
    }

    // Nuevo endpoint para verificar si existe una reserva
    @GetMapping("/existe")
    public ResponseEntity<Boolean> existeReserva(@RequestParam Long actividadId, @RequestParam Long consumidorId) {
        logger.info("Solicitud para verificar si existe una reserva: actividad ID {}, consumidor ID {}", actividadId, consumidorId);
        boolean existe = reservaService.existeReserva(actividadId, consumidorId);
        return ResponseEntity.ok(existe);
    }
}
