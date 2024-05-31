package com.proyectoIntegradoManuel.pi.services;

import com.proyectoIntegradoManuel.pi.repository.ReservaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.proyectoIntegradoManuel.pi.model.Actividad;
import com.proyectoIntegradoManuel.pi.repository.ActividadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service public class ActividadService {

    // Logger para registrar información, advertencias y errores en el servicio
    static final Logger logger = LoggerFactory.getLogger(ActividadService.class);

    // Inyección de dependencias del repositorio de actividades
    @Autowired
    private ActividadRepository actividadRepository;

    // Método para obtener todas las actividades con paginación
    public Page<Actividad> obtenerTodasLasActividades(Pageable pageable) {
        // Llama al repositorio para obtener todas las actividades paginadas
        return actividadRepository.findAll(pageable);
    }

    // Método para obtener actividades por ID de ofertante con paginación
    public Page<Actividad> findByOfertanteId(Long ofertanteId, Pageable pageable) {
        // Llama al repositorio para obtener actividades por el ID del ofertante con paginación
        return actividadRepository.findByOfertanteId(ofertanteId, pageable);
    }

    // Método para guardar una nueva actividad
    public Actividad save(Actividad actividad) {
        // Llama al repositorio para guardar la actividad y retorna la actividad guardada
        return actividadRepository.save(actividad);
    }

    // Método para eliminar una actividad por su ID
    public void deleteById(Long id) {
        // Llama al repositorio para eliminar la actividad por su ID
        actividadRepository.deleteById(id);
    }

    // Método para encontrar una actividad por su ID
    public Optional<Actividad> findById(Long id) {
        // Llama al repositorio para encontrar la actividad por su ID y retorna un Optional de la actividad
        return actividadRepository.findById(id);
    }
}