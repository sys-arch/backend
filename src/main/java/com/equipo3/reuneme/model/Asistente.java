package com.equipo3.reuneme.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity
@IdClass(AsistenteId.class)
public class Asistente {

    @Id
    private Long idReunion;

    @Id
    @Column(length = 36)
    private String idUsuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAsistente estado;

    @Column(nullable = false)
    private Boolean asiste;

	public Asistente() {}

	public Asistente(Long idReunion, String idUsuario, EstadoAsistente estado, Boolean asiste) {
		this.idReunion = idReunion;
		this.idUsuario = idUsuario;
		this.estado = estado;
		this.asiste = asiste;
	}

	public Long getIdReunion() {
		return idReunion;
	}

	public void setIdReunion(Long idReunion) {
		this.idReunion = idReunion;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public EstadoAsistente getEstado() {
		return estado;
	}

	public void setEstado(EstadoAsistente estado) {
		this.estado = estado;
	}

	public Boolean getAsiste() {
		return asiste;
	}

	public void setAsiste(Boolean asiste) {
		this.asiste = asiste;
	}
   
}
