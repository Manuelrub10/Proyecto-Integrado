package com.proyectoIntegradoManuel.pi.repository;

import com.proyectoIntegradoManuel.pi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Método para encontrar un usuario por su email
    Usuario findByEmail(String email);
}