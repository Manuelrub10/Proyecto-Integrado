package com.proyectoIntegradoManuel.pi.model.dto;

// DTO para transferir datos de la entidad Actividad
public class ActividadDTO {
    private String titulo;
    private String descripcion;
    private int duracion;
    private int numMinParticipantes;
    private int numMaxParticipantes;
    private String fecha; // Mantenemos como String
    private String lugar;
    private String materialNecesario;
    private String materialOfrecido;
    private String tipo;
    private Long ofertanteId; // Añadir el ofertanteId

    // Getters y Setters

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

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getOfertanteId() {
        return ofertanteId;
    }

    public void setOfertanteId(Long ofertanteId) {
        this.ofertanteId = ofertanteId;
    }
}