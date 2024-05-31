package com.proyectoIntegradoManuel.pi.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
// Configuración para manejar referencias cíclicas durante la serialización de JSON
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Actividad {

    // Anotación que define el campo 'id' como la clave primaria
    @Id
    // Anotación que define la estrategia de generación de la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descripcion;
    private int duracion;
    private int numMinParticipantes;
    private int numMaxParticipantes;
    private LocalDateTime fecha;
    private String lugar;
    private String materialNecesario;
    private String materialOfrecido;
    private String tipo;

    // Anotación que define una relación muchos a uno con la entidad 'Ofertante'
    @ManyToOne(fetch = FetchType.LAZY)
    // Define la columna que almacenará la clave foránea hacia la entidad 'Ofertante'
    @JoinColumn(name = "ofertante_id")
    // Configuración para manejar referencias cíclicas durante la serialización de JSON
    @JsonBackReference
    private Ofertante ofertante;

    // Anotación que define una relación uno a muchos con la entidad 'Reserva'
    @OneToMany(mappedBy = "actividad")
    // Indica que este campo debe ser ignorado durante la serialización JSON
    @JsonIgnore
    private List<Reserva> reservas;

    // Constructor vacío requerido por JPA
    public Actividad() {
    }

    // Constructor con parámetros
    public Actividad(Long id, Ofertante ofertante, String titulo, String descripcion, String tipo, String lugar, LocalDateTime fecha, int duracion, String materialNecesario, String materialOfrecido, int numMinParticipantes, int numMaxParticipantes, List<Reserva> reservas) {
        this.id = id;
        this.ofertante = ofertante;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.lugar = lugar;
        this.fecha = fecha;
        this.duracion = duracion;
        this.materialNecesario = materialNecesario;
        this.materialOfrecido = materialOfrecido;
        this.numMinParticipantes = numMinParticipantes;
        this.numMaxParticipantes = numMaxParticipantes;
        this.reservas = reservas;
    }

    // Getters y setters para cada campo

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ofertante getOfertante() {
        return ofertante;
    }

    public void setOfertante(Ofertante ofertante) {
        this.ofertante = ofertante;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getMaterialNecesario() {
        return materialNecesario;
    }

    public void setMaterialNecesario(String materialNecesario) {
        this.materialNecesario = materialNecesario;
    }

    public String getMaterialOfrecido() {
        return materialOfrecido;
    }

    public void setMaterialOfrecido(String materialOfrecido) {
        this.materialOfrecido = materialOfrecido;
    }

    public int getNumMinParticipantes() {
        return numMinParticipantes;
    }

    public void setNumMinParticipantes(int numMinParticipantes) {
        this.numMinParticipantes = numMinParticipantes;
    }

    public int getNumMaxParticipantes() {
        return numMaxParticipantes;
    }

    public void setNumMaxParticipantes(int numMaxParticipantes) {
        this.numMaxParticipantes = numMaxParticipantes;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
}