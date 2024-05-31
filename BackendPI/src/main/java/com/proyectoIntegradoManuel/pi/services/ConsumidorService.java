package com.proyectoIntegradoManuel.pi.services;

import com.proyectoIntegradoManuel.pi.config.ForeignKeyException;
import com.proyectoIntegradoManuel.pi.model.Actividad;
import com.proyectoIntegradoManuel.pi.model.Consumidor;
import com.proyectoIntegradoManuel.pi.model.Ofertante;
import com.proyectoIntegradoManuel.pi.model.Usuario;
import com.proyectoIntegradoManuel.pi.repository.ConsumidorRepository;
import com.proyectoIntegradoManuel.pi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConsumidorService {

    // Inyección de dependencias del repositorio de consumidores
    @Autowired
    private ConsumidorRepository consumidorRepository;

    // Inyección de dependencias del repositorio de usuarios
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método para encontrar un consumidor por su ID
    public Consumidor findById(Long id) {
        return consumidorRepository.findById(id).orElse(null);  // Retorna null si no se encuentra el consumidor
    }

    // Método para eliminar un consumidor por su entidad
    public void delete(Consumidor consumidor) {
        consumidorRepository.delete(consumidor);
    }

    // Método para eliminar un consumidor por su ID
    public void deleteById(Long id) {
        try {
            consumidorRepository.deleteConsumidorById(id);  // Intentar eliminar el consumidor
        } catch (DataIntegrityViolationException e) {
            // Lanzar una excepción personalizada si hay una violación de integridad de datos
            throw new ForeignKeyException("No se ha podido borrar al consumidor con id " + id + " porque tiene reservas asociadas.", HttpStatus.CONFLICT);
        }
    }

    // Método para guardar un consumidor en el repositorio
    public Consumidor save(Consumidor consumidor) {
        return consumidorRepository.save(consumidor);
    }

    // Método para actualizar un consumidor existente
    public Consumidor updateConsumidor(Consumidor consumidorDetails) {
        // Buscar el consumidor por su ID, retornar null si no se encuentra
        Consumidor consumidor = consumidorRepository.findById(consumidorDetails.getId()).orElse(null);

        // Si el consumidor existe
        if (consumidor != null) {
            // Obtener el usuario asociado al consumidor
            Usuario usuario = consumidor.getUsuario();
            if (usuario != null) {
                // Actualizar los datos del usuario
                usuario.setNombre(consumidorDetails.getNombre());
                usuario.setApellido(consumidorDetails.getApellidos());
                usuarioRepository.save(usuario);  // Guardar el usuario actualizado
            }

            // Actualizar los datos del consumidor
            consumidor.setNombre(consumidorDetails.getNombre());
            consumidor.setApellidos(consumidorDetails.getApellidos());
            consumidor.setDireccion(consumidorDetails.getDireccion());
            consumidor.setTelefono(consumidorDetails.getTelefono());
            consumidorRepository.save(consumidor);  // Guardar el consumidor actualizado
        }
        return consumidor;  // Retornar el consumidor actualizado (o null si no se encontró)
    }
}