package com.proyectoIntegradoManuel.pi.services;

import com.proyectoIntegradoManuel.pi.config.CustomException;
import com.proyectoIntegradoManuel.pi.model.Actividad;
import com.proyectoIntegradoManuel.pi.model.Consumidor;
import com.proyectoIntegradoManuel.pi.model.Reserva;
import com.proyectoIntegradoManuel.pi.repository.ActividadRepository;
import com.proyectoIntegradoManuel.pi.repository.ConsumidorRepository;
import com.proyectoIntegradoManuel.pi.repository.ReservaRepository;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaService {

    // Logger para registrar información, advertencias y errores en el servicio
    private static final Logger logger = LoggerFactory.getLogger(ReservaService.class);

    // Inyección de dependencias del repositorio de reservas
    @Autowired
    ReservaRepository reservaRepository;

    // Inyección de dependencias del repositorio de actividades
    @Autowired
    private ActividadRepository actividadRepository;

    // Inyección de dependencias del repositorio de consumidores
    @Autowired
    private ConsumidorRepository consumidorRepository;

    // Método para crear una nueva reserva
    public Reserva createReserva(Long actividadId, Long consumidorId) {
        logger.info("Creando reserva para actividad ID {} y consumidor ID {}", actividadId, consumidorId);

        // Buscar la actividad por su ID, lanzar excepción si no se encuentra
        Actividad actividad = actividadRepository.findById(actividadId)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

        // Buscar el consumidor por su ID, lanzar excepción si no se encuentra
        Consumidor consumidor = consumidorRepository.findById(consumidorId)
                .orElseThrow(() -> new RuntimeException("Consumidor no encontrado"));

        // Verificar si la fecha de la actividad ya ha pasado, lanzar excepción si es así
        if (actividad.getFecha().isBefore(LocalDateTime.now())) {
            throw new CustomException("No se puede reservar para una actividad cuya fecha ya ha pasado", HttpStatus.BAD_REQUEST);
        }

        // Verificar si ya existe una reserva para la misma actividad y consumidor, lanzar excepción si es así
        boolean existeReserva = reservaRepository.existsByActividadIdAndConsumidorId(actividadId, consumidorId);
        if (existeReserva) {
            throw new CustomException("Ya tienes una reserva para esta actividad", HttpStatus.BAD_REQUEST);
        }

        // Verificar si hay plazas disponibles en la actividad, lanzar excepción si no hay
        if (actividad.getReservas().size() >= actividad.getNumMaxParticipantes()) {
            throw new CustomException("No hay plazas disponibles", HttpStatus.BAD_REQUEST);
        }

        // Crear y guardar la nueva reserva
        Reserva reserva = new Reserva();
        reserva.setActividad(actividad);
        reserva.setConsumidor(consumidor);
        reserva.setFechaReserva(LocalDateTime.now());

        Reserva savedReserva = reservaRepository.save(reserva);
        logger.info("Reserva creada con éxito: {}", savedReserva);

        return savedReserva;
    }

    // Método para obtener reservas de un consumidor con paginación
    public Page<Reserva> getReservasByConsumidor(Long consumidorId, Pageable pageable) {
        logger.info("Obteniendo reservas para el consumidor ID {} con paginación: página {}, tamaño {}", consumidorId, pageable.getPageNumber(), pageable.getPageSize());
        return reservaRepository.findByConsumidorId(consumidorId, pageable);
    }

    // Método para obtener reservas de una actividad
    public List<Reserva> getReservasByActividad(Long actividadId) {
        logger.info("Obteniendo reservas para la actividad ID {}", actividadId);
        return reservaRepository.findByActividadId(actividadId);
    }

    // Método para cancelar una reserva por su ID
    public void cancelReserva(Long reservaId) {
        logger.info("Cancelando reserva ID {}", reservaId);
        reservaRepository.deleteById(reservaId);
    }

    // Método para verificar si existe una reserva para una actividad específica
    public boolean existeReservaIdActividad(Long actividadId) {
        logger.info("Verificando si existe una reserva para actividad ID {}", actividadId);
        return reservaRepository.existsByActividad_Id(actividadId);
    }

    // Método para verificar si existe una reserva para una actividad y consumidor específicos
    public boolean existeReserva(Long actividadId, Long consumidorId) {
        logger.info("Verificando si existe una reserva para actividad ID {} y consumidor ID {}", actividadId, consumidorId);
        return reservaRepository.existsByActividadIdAndConsumidorId(actividadId, consumidorId);
    }
}