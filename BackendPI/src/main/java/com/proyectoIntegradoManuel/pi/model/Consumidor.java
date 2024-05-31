package com.proyectoIntegradoManuel.pi.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.util.List;

@Entity
// Configuración para manejar referencias cíclicas durante la serialización de JSON
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Consumidor {

    // Anotación que define el campo 'id' como la clave primaria
    @Id
    // Anotación que define la estrategia de generación de la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Anotación que define una relación uno a uno con la entidad 'Usuario'
    @OneToOne
    // Define la columna que almacenará la clave foránea hacia la entidad 'Usuario'
    @JoinColumn(name = "usuario_id")
    // Configuración para manejar referencias cíclicas durante la serialización de JSON
    @JsonBackReference
    private Usuario usuario;

    private String nombre;
    private String apellidos;
    private String telefono;
    private String direccion;

    // Anotación que define una relación uno a muchos con la entidad 'Reserva'
    @OneToMany(mappedBy = "consumidor")
    // Indica que este campo debe ser ignorado durante la serialización JSON
    @JsonIgnore
    private List<Reserva> reservas;

    // Constructor vacío requerido por JPA
    public Consumidor() {
    }

    // Constructor con parámetros
    public Consumidor(Long id, Usuario usuario, String nombre, String apellidos, String telefono, String direccion, List<Reserva> reservas) {
        this.id = id;
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.direccion = direccion;
        this.reservas = reservas;
    }

    // Getters y setters para cada campo

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
}