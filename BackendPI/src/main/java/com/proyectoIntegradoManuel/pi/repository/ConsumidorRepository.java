package com.proyectoIntegradoManuel.pi.repository;

import com.proyectoIntegradoManuel.pi.model.Consumidor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumidorRepository extends JpaRepository<Consumidor, Long> {
    // Anotación que indica que este método modifica datos en la base de datos
    @Modifying
    // Anotación que indica que el método debe ejecutarse dentro de una transacción
    @Transactional
    // Consulta personalizada para eliminar un consumidor por su ID
    @Query("DELETE FROM Consumidor c WHERE c.id = :id")
    void deleteConsumidorById(@Param("id") Long id);
}