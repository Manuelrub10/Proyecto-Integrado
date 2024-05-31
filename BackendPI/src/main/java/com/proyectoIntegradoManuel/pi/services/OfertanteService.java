package com.proyectoIntegradoManuel.pi.services;

import com.proyectoIntegradoManuel.pi.config.CustomException;
import com.proyectoIntegradoManuel.pi.config.ForeignKeyException;
import com.proyectoIntegradoManuel.pi.model.Consumidor;
import com.proyectoIntegradoManuel.pi.model.Ofertante;
import com.proyectoIntegradoManuel.pi.model.Usuario;
import com.proyectoIntegradoManuel.pi.repository.OfertanteRepository;
import com.proyectoIntegradoManuel.pi.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class OfertanteService {

    // Inyección de dependencias del repositorio de ofertantes
    @Autowired
    private OfertanteRepository ofertanteRepository;

    // Inyección de dependencias del repositorio de usuarios
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método para encontrar un ofertante por su ID
    public Ofertante findById(Long id) {
        return ofertanteRepository.findById(id).orElse(null);  // Retorna null si no se encuentra el ofertante
    }

    // Método para guardar un ofertante en el repositorio
    public Ofertante save(Ofertante ofertante) {
        return ofertanteRepository.save(ofertante);
    }

    // Método para actualizar un ofertante existente
    public Ofertante updateOfertante(Ofertante ofertanteDetails) {
        // Buscar el ofertante por su ID, retornar null si no se encuentra
        Ofertante ofertante = ofertanteRepository.findById(ofertanteDetails.getId()).orElse(null);

        // Si el ofertante existe
        if (ofertante != null) {
            // Obtener el usuario asociado al ofertante
            Usuario usuario = ofertante.getUsuario();
            if (usuario != null) {
                // Actualizar los datos del usuario
                usuario.setNombre(ofertanteDetails.getNombre());
                usuario.setApellido(ofertanteDetails.getApellidos());
                usuarioRepository.save(usuario);  // Guardar el usuario actualizado
            }

            // Actualizar los datos del ofertante
            ofertante.setNombre(ofertanteDetails.getNombre());
            ofertante.setApellidos(ofertanteDetails.getApellidos());
            ofertante.setDireccion(ofertanteDetails.getDireccion());
            ofertante.setTelefono(ofertanteDetails.getTelefono());
            ofertante.setDescripcion(ofertanteDetails.getDescripcion());
            ofertanteRepository.save(ofertante);  // Guardar el ofertante actualizado
        }
        return ofertante;  // Retornar el ofertante actualizado (o null si no se encontró)
    }

    // Método para eliminar un ofertante por su ID
    public void deleteById(Long id) {
        try {
            ofertanteRepository.deleteOfertanteById(id);  // Intentar eliminar el ofertante
        } catch (DataIntegrityViolationException e) {
            // Lanzar una excepción personalizada si hay una violación de integridad de datos
            throw new ForeignKeyException("No se ha podido borrar al ofertante con id " + id + " porque tiene actividades asociadas.", HttpStatus.CONFLICT);
        }
    }
}