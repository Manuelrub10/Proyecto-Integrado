package com.proyectoIntegradoManuel.pi.controller;

import com.proyectoIntegradoManuel.pi.model.Actividad;
import com.proyectoIntegradoManuel.pi.model.Ofertante;
import com.proyectoIntegradoManuel.pi.model.dto.ActividadDTO;
import com.proyectoIntegradoManuel.pi.services.ActividadService;
import com.proyectoIntegradoManuel.pi.services.OfertanteService;
import com.proyectoIntegradoManuel.pi.services.ReservaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/actividades")
// Permite solicitudes CORS desde el origen especificado
@CrossOrigin(origins = "http://localhost:4200")
public class ActividadController {

    // Inyección de dependencias
    @Autowired
    private ActividadService actividadService;

    @Autowired
    private OfertanteService ofertanteService;

    @Autowired
    private ReservaService reservaService;

    // Logger para registrar información, advertencias y errores en el controlador
    static final Logger logger = LoggerFactory.getLogger(ActividadController.class);

    // Endpoint para obtener actividades por ID de ofertante con paginación
    @GetMapping("/ofertante/{ofertanteId}")
    public ResponseEntity<Page<Actividad>> obtenerActividadesPorOfertante(
            @PathVariable Long ofertanteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        // Crear el objeto Pageable para la paginación
        Pageable pageable = PageRequest.of(page, size);
        // Llamar al servicio para obtener las actividades del ofertante con paginación
        Page<Actividad> actividadesPage = actividadService.findByOfertanteId(ofertanteId, pageable);
        // Retornar la respuesta con el objeto Page de actividades
        return ResponseEntity.ok(actividadesPage);
    }

    // Endpoint para crear una nueva actividad
    @PostMapping("/crear")
    public ResponseEntity<?> crearActividad(@RequestBody ActividadDTO actividadDTO) {
        // Registrar información sobre la creación de la actividad
        logger.info("Creando actividad con datos: {}", actividadDTO);

        // Crear una nueva instancia de Actividad y asignar valores desde el DTO
        Actividad actividad = new Actividad();
        actividad.setTitulo(actividadDTO.getTitulo());
        actividad.setDescripcion(actividadDTO.getDescripcion());
        actividad.setDuracion(actividadDTO.getDuracion());
        actividad.setNumMinParticipantes(actividadDTO.getNumMinParticipantes());
        actividad.setNumMaxParticipantes(actividadDTO.getNumMaxParticipantes());

        // Formatear y parsear la fecha desde el DTO
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime fecha = LocalDateTime.parse(actividadDTO.getFecha(), formatter);
        actividad.setFecha(fecha);

        actividad.setLugar(actividadDTO.getLugar());
        actividad.setMaterialNecesario(actividadDTO.getMaterialNecesario());
        actividad.setMaterialOfrecido(actividadDTO.getMaterialOfrecido());
        actividad.setTipo(actividadDTO.getTipo());

        // Buscar el ofertante por su ID
        Ofertante ofertante = ofertanteService.findById(actividadDTO.getOfertanteId());
        if (ofertante != null) {
            // Asignar el ofertante y guardar la nueva actividad
            actividad.setOfertante(ofertante);
            Actividad nuevaActividad = actividadService.save(actividad);
            logger.info("Actividad creada exitosamente: {}", nuevaActividad);
            return ResponseEntity.ok(nuevaActividad);
        } else {
            // Registrar advertencia y retornar error si el ofertante no se encuentra
            logger.warn("Ofertante no encontrado para id: {}", actividadDTO.getOfertanteId());
            return ResponseEntity.badRequest().body("Ofertante no encontrado");
        }
    }

    // Endpoint para editar una actividad existente
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarActividad(@PathVariable Long id, @RequestBody ActividadDTO actividadDTO) {
        // Registrar información sobre la edición de la actividad
        logger.info("Editando actividad con id: {}", id);

        // Buscar la actividad por su ID
        return actividadService.findById(id)
                .map(actividad -> {
                    // Asignar nuevos valores desde el DTO
                    actividad.setTitulo(actividadDTO.getTitulo());
                    actividad.setDescripcion(actividadDTO.getDescripcion());
                    actividad.setDuracion(actividadDTO.getDuracion());
                    actividad.setNumMinParticipantes(actividadDTO.getNumMinParticipantes());
                    actividad.setNumMaxParticipantes(actividadDTO.getNumMaxParticipantes());

                    // Formatear y parsear la fecha desde el DTO
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                    LocalDateTime fecha = LocalDateTime.parse(actividadDTO.getFecha(), formatter);
                    actividad.setFecha(fecha);

                    actividad.setLugar(actividadDTO.getLugar());
                    actividad.setMaterialNecesario(actividadDTO.getMaterialNecesario());
                    actividad.setMaterialOfrecido(actividadDTO.getMaterialOfrecido());
                    actividad.setTipo(actividadDTO.getTipo());

                    // Guardar la actividad actualizada y retornar la respuesta
                    Actividad actividadActualizada = actividadService.save(actividad);
                    logger.info("Actividad actualizada exitosamente: {}", actividadActualizada);
                    return ResponseEntity.ok(actividadActualizada);
                })
                .orElse(ResponseEntity.notFound().build()); // Retornar error si la actividad no se encuentra
    }

    // Endpoint para eliminar una actividad por su ID
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarActividad(@PathVariable Long id) {
        try {
            // Buscar la actividad por su ID
            Optional<Actividad> optionalActividad = actividadService.findById(id);
            if (optionalActividad.isPresent()) {
                // Verificar si la actividad tiene reservas asociadas
                if (reservaService.existeReservaIdActividad(id)){
                    return ResponseEntity.badRequest().body("Esta actividad tiene reservas");
                }
                // Eliminar la actividad y retornar la respuesta
                actividadService.deleteById(id);
                logger.info("Actividad eliminada exitosamente: {}", optionalActividad.get());
                return ResponseEntity.ok().build();
            } else {
                // Registrar advertencia y retornar error si la actividad no se encuentra
                logger.warn("Actividad no encontrada para id: {}", id);
                return ResponseEntity.badRequest().body("Actividad no encontrada");
            }
        } catch (DataIntegrityViolationException e) {
            // Manejar error de violación de integridad de datos y retornar conflicto
            logger.error("Error al eliminar actividad debido a reservas activas:", e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No se puede eliminar la actividad porque tiene reservas activas.");
        } catch (Exception e) {
            // Manejar error genérico y retornar error interno del servidor
            logger.error("Error al eliminar actividad:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    // Endpoint para obtener todas las actividades con paginación
    @GetMapping("/todas")
    public ResponseEntity<Page<Actividad>> obtenerTodasLasActividades(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        // Crear el objeto Pageable para la paginación
        Pageable pageable = PageRequest.of(page, size);
        // Llamar al servicio para obtener todas las actividades con paginación
        Page<Actividad> actividadesPage = actividadService.obtenerTodasLasActividades(pageable);
        // Retornar la respuesta con el objeto Page de actividades
        return ResponseEntity.ok(actividadesPage);
    }

    // Endpoint para obtener una actividad por su ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerActividadPorId(@PathVariable Long id) {
        // Registrar información sobre la obtención de la actividad
        logger.info("Obteniendo actividad con id: {}", id);
        // Buscar la actividad por su ID y retornar la respuesta
        return actividadService.findById(id)
                .map(actividad -> ResponseEntity.ok().body(actividad))
                .orElse(ResponseEntity.notFound().build()); // Retornar error si la actividad no se encuentra
    }
}