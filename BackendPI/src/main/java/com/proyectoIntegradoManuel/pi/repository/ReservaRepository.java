package com.proyectoIntegradoManuel.pi.repository;

import com.proyectoIntegradoManuel.pi.model.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Método para encontrar reservas por el ID de la actividad
    List<Reserva> findByActividadId(Long actividadId);

    // Método para encontrar reservas por el ID del consumidor con paginación
    Page<Reserva> findByConsumidorId(Long consumidorId, Pageable pageable);

    // Método para verificar si existe una reserva para una actividad específica
    boolean existsByActividad_Id(@Param("actividadId") Long actividadId);

    // Método para verificar si existe una reserva para una actividad y consumidor específicos
    boolean existsByActividadIdAndConsumidorId(Long actividadId, Long consumidorId); // Nueva línea
}