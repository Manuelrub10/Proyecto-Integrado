package com.proyectoIntegradoManuel.pi.controller;

import com.proyectoIntegradoManuel.pi.model.Consumidor;
import com.proyectoIntegradoManuel.pi.model.Ofertante;
import com.proyectoIntegradoManuel.pi.model.dto.JwtResponse;
import com.proyectoIntegradoManuel.pi.model.dto.LoginRequest;
import com.proyectoIntegradoManuel.pi.model.Usuario;
import com.proyectoIntegradoManuel.pi.model.dto.RegisterRequest;
import com.proyectoIntegradoManuel.pi.services.ConsumidorService;
import com.proyectoIntegradoManuel.pi.services.OfertanteService;
import com.proyectoIntegradoManuel.pi.services.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
// Permite solicitudes CORS desde el origen especificado
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/usuarios")
public class UsuarioController {

    // Inyección de dependencias
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private OfertanteService ofertanteService;

    @Autowired
    private ConsumidorService consumidorService;

    // Logger para registrar información, advertencias y errores en el controlador
    static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    // Endpoint para autenticar un usuario
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        logger.info("Intentando autenticar usuario con email: {}", loginRequest.getEmail());

        // Buscar el usuario por su email utilizando el servicio
        Usuario usuario = usuarioService.findByEmail(loginRequest.getEmail());
        if (usuario != null) {
            logger.info("Usuario encontrado: {}", usuario.getEmail());

            // Verificar la contraseña
            if (usuario.getPassword().equals(loginRequest.getPassword())) {
                logger.info("Contraseña correcta para usuario: {}", usuario.getEmail());

                boolean rolAutorizado = false;

                // Verificar y asignar el rol de consumidor
                if ("consumidor".equalsIgnoreCase(loginRequest.getRole())) {
                    if (usuario.getConsumidor() != null) {
                        rolAutorizado = true;
                    } else {
                        Consumidor nuevoConsumidor = new Consumidor();
                        nuevoConsumidor.setUsuario(usuario);
                        nuevoConsumidor.setNombre(usuario.getNombre());
                        nuevoConsumidor.setApellidos(usuario.getApellido());
                        nuevoConsumidor.setDireccion(usuario.getDireccion());
                        nuevoConsumidor.setTelefono(usuario.getTelefono());
                        consumidorService.save(nuevoConsumidor);
                        rolAutorizado = true;
                        logger.info("Rol de consumidor creado para el usuario: {}", usuario.getEmail());
                    }
                    // Verificar y asignar el rol de ofertante
                } else if ("ofertante".equalsIgnoreCase(loginRequest.getRole())) {
                    if (usuario.getOfertante() != null) {
                        rolAutorizado = true;
                    } else {
                        Ofertante nuevoOfertante = new Ofertante();
                        nuevoOfertante.setUsuario(usuario);
                        nuevoOfertante.setNombre(usuario.getNombre());
                        nuevoOfertante.setApellidos(usuario.getApellido());
                        nuevoOfertante.setDireccion(usuario.getDireccion());
                        nuevoOfertante.setTelefono(usuario.getTelefono());
                        nuevoOfertante.setDescripcion("");
                        ofertanteService.save(nuevoOfertante);
                        rolAutorizado = true;
                        logger.info("Rol de ofertante creado para el usuario: {}", usuario.getEmail());
                    }
                }

                // Verificar si el rol es autorizado
                if (!rolAutorizado) {
                    logger.warn("Rol no autorizado: {} para usuario: {}", loginRequest.getRole(), usuario.getEmail());
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Rol no autorizado");
                }

                // Generar un token JWT
                String token = "jwt-token";
                logger.info("Usuario autenticado: {} como {}", usuario.getEmail(), loginRequest.getRole());
                return ResponseEntity.ok(new JwtResponse(token, usuario));
            } else {
                logger.warn("Contraseña incorrecta para usuario: {}", usuario.getEmail());
            }
        } else {
            logger.warn("Usuario no encontrado con email: {}", loginRequest.getEmail());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Correo o contraseña incorrectos");
    }

    // Endpoint para registrar un nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        logger.info("Intentando registrar usuario con email: {}", registerRequest.getEmail());

        // Verificar si el usuario ya existe
        if (usuarioService.findByEmail(registerRequest.getEmail()) != null) {
            logger.warn("Usuario ya existe con email: {}", registerRequest.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Ese correo ya ha sido registrado."));
        }

        // Registrar el nuevo usuario
        Usuario usuario = usuarioService.register(registerRequest);
        logger.info("Usuario registrado: {}", usuario.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", "Usuario registrado exitosamente"));
    }

    // Endpoint para obtener un usuario por su email
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUsuarioByEmail(@PathVariable String email) {
        Usuario usuario = usuarioService.findByEmail(email);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

    // Endpoint para obtener un usuario por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        Usuario usuario = usuarioService.getUsuarioById(id);
        return ResponseEntity.ok(usuario);
    }
}