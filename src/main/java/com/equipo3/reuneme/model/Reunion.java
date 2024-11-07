package com.equipo3.reuneme.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table (name="reuniones")
public class Reunion {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "organizador_id", nullable = false)
    private Usuario organizador;

    @Column(nullable = false)
    private LocalDateTime horaInicio;

    @Column(nullable = false)
    private LocalDateTime horaFin;

    @Column(nullable = false)
    private String ubicacion;

    @Column(nullable = false)
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoReunion estado;

	public Reunion() {}

	public Reunion(Long id, Usuario organizador, LocalDateTime horaInicio, LocalDateTime horaFin, String ubicacion, String observaciones, EstadoReunion estado) {
		this.id = id;
		this.organizador = organizador;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.ubicacion = ubicacion;
		this.observaciones = observaciones;
		this.estado = estado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getOrganizador() {
		return organizador;
	}

	public void setOrganizador(Usuario organizador) {
		this.organizador = organizador;
	}

	public LocalDateTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalDateTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public LocalDateTime getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(LocalDateTime horaFin) {
		this.horaFin = horaFin;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public EstadoReunion getEstado() {
		return estado;
	}

	public void setEstado(EstadoReunion estado) {
		this.estado = estado;
	}
    
}

