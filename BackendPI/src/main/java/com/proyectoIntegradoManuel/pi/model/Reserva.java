package com.proyectoIntegradoManuel.pi.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Reserva {

    // Anotación que define el campo 'id' como la clave primaria
    @Id
    // Anotación que define la estrategia de generación de la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Anotación que define una relación muchos a uno con la entidad 'Consumidor'
    @ManyToOne
    // Define la columna que almacenará la clave foránea hacia la entidad 'Consumidor'
    @JoinColumn(name = "consumidor_id")
    private Consumidor consumidor;

    // Anotación que define una relación muchos a uno con la entidad 'Actividad'
    @ManyToOne
    // Define la columna que almacenará la clave foránea hacia la entidad 'Actividad'
    @JoinColumn(name = "actividad_id")
    private Actividad actividad;

    // Campo que almacena la fecha y hora de la reserva
    private LocalDateTime fechaReserva;

    // Constructor vacío requerido por JPA
    public Reserva() {
    }

    // Constructor con parámetros
    public Reserva(Long id, Consumidor consumidor, Actividad actividad, LocalDateTime fechaReserva) {
        this.id = id;
        this.consumidor = consumidor;
        this.actividad = actividad;
        this.fechaReserva = fechaReserva;
    }

    // Getters y setters para cada campo
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Consumidor getConsumidor() {
        return consumidor;
    }

    public void setConsumidor(Consumidor consumidor) {
        this.consumidor = consumidor;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }
}