package com.proyectoIntegradoManuel.pi.repository;

import com.proyectoIntegradoManuel.pi.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    // Método para obtener todas las actividades con paginación
    Page<Actividad> findAll(Pageable pageable);

    // Método para obtener actividades por ID de ofertante con paginación
    Page<Actividad> findByOfertanteId(Long ofertanteId, Pageable pageable);
}