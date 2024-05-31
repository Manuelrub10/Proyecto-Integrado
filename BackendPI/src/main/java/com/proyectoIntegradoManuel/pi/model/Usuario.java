package com.proyectoIntegradoManuel.pi.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

@Entity
// Configuración para manejar referencias cíclicas durante la serialización de JSON
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Usuario {

    // Anotación que define el campo 'id' como la clave primaria
    @Id
    // Anotación que define la estrategia de generación de la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String direccion;
    private String telefono;

    // Anotación que define una relación uno a uno con la entidad 'Consumidor'
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // Configuración para manejar referencias cíclicas durante la serialización de JSON
    @JsonManagedReference
    private Consumidor consumidor;

    // Anotación que define una relación uno a uno con la entidad 'Ofertante'
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // Configuración para manejar referencias cíclicas durante la serialización de JSON
    @JsonManagedReference
    private Ofertante ofertante;

    // Constructor vacío requerido por JPA
    public Usuario() {
    }

    // Constructor con parámetros
    public Usuario(Long id, String nombre, String apellido, String email, String password, String direccion, String telefono, Consumidor consumidor, Ofertante ofertante) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.direccion = direccion;
        this.telefono = telefono;
        this.consumidor = consumidor;
        this.ofertante = ofertante;
    }

    // Getters y setters para cada campo
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Consumidor getConsumidor() {
        return consumidor;
    }

    public void setConsumidor(Consumidor consumidor) {
        this.consumidor = consumidor;
    }

    public Ofertante getOfertante() {
        return ofertante;
    }

    public void setOfertante(Ofertante ofertante) {
        this.ofertante = ofertante;
    }

}