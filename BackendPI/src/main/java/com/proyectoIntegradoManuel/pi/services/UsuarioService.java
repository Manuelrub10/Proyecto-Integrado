package com.proyectoIntegradoManuel.pi.services;

import com.proyectoIntegradoManuel.pi.model.Consumidor;
import com.proyectoIntegradoManuel.pi.model.Ofertante;
import com.proyectoIntegradoManuel.pi.model.Usuario;
import com.proyectoIntegradoManuel.pi.model.dto.RegisterRequest;
import com.proyectoIntegradoManuel.pi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {

    // Inyección de dependencias del repositorio de usuarios
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Inyección de dependencias del servicio de consumidores
    @Autowired
    private ConsumidorService consumidorService;

    // Inyección de dependencias del servicio de ofertantes
    @Autowired
    private OfertanteService ofertanteService;

    // Método para encontrar un usuario por su email
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Método para guardar un usuario en el repositorio
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Método para registrar un nuevo usuario a partir de una solicitud de registro
    public Usuario register(RegisterRequest registerRequest) {
        // Crear una nueva instancia de Usuario y establecer sus propiedades a partir de la solicitud de registro
        Usuario usuario = new Usuario();
        usuario.setNombre(registerRequest.getNombre());
        usuario.setApellido(registerRequest.getApellido());
        usuario.setEmail(registerRequest.getEmail());
        usuario.setPassword(registerRequest.getPassword());
        usuario.setDireccion(registerRequest.getDireccion());
        usuario.setTelefono(registerRequest.getTelefono());

        // Guardar el nuevo usuario en el repositorio y obtener la instancia guardada
        Usuario savedUsuario = usuarioRepository.save(usuario);

        // Comprobar el rol del usuario y crear una entidad específica según el rol
        if ("consumidor".equalsIgnoreCase(registerRequest.getRole())) {
            Consumidor consumidor = new Consumidor();
            consumidor.setUsuario(savedUsuario);
            consumidor.setNombre(registerRequest.getNombre());
            consumidor.setApellidos(registerRequest.getApellido());
            consumidor.setDireccion(registerRequest.getDireccion());
            consumidor.setTelefono(registerRequest.getTelefono());
            consumidorService.save(consumidor);  // Guardar el consumidor en su respectivo servicio
        } else if ("ofertante".equalsIgnoreCase(registerRequest.getRole())) {
            Ofertante ofertante = new Ofertante();
            ofertante.setUsuario(savedUsuario);
            ofertante.setNombre(registerRequest.getNombre());
            ofertante.setApellidos(registerRequest.getApellido());
            ofertante.setDescripcion("");  // Establecer una descripción vacía por defecto
            ofertante.setDireccion(registerRequest.getDireccion());
            ofertante.setTelefono(registerRequest.getTelefono());
            ofertanteService.save(ofertante);  // Guardar el ofertante en su respectivo servicio
        }

        // Retornar la instancia del usuario guardado
        return savedUsuario;
    }

    // Método para obtener un usuario por su ID
    public Usuario getUsuarioById(Long id) {
        return usuarioRepository.findById(id).orElse(null);  // Retorna null si no se encuentra el usuario
    }

}