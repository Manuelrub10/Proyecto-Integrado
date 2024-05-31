package com.proyectoIntegradoManuel.pi.repository;

import com.proyectoIntegradoManuel.pi.model.Ofertante;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OfertanteRepository extends JpaRepository<Ofertante, Long> {

    // Anotación que indica que este método modifica datos en la base de datos
    @Modifying
    // Anotación que indica que el método debe ejecutarse dentro de una transacción
    @Transactional
    // Consulta personalizada para eliminar un ofertante por su ID
    @Query("DELETE FROM Ofertante o WHERE o.id = :id")
    void deleteOfertanteById(@Param("id") Long id);
}