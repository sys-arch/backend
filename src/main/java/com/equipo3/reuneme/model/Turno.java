package com.equipo3.reuneme.model;

import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "turnos")
public class Turno {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="hora_inicio", nullable = false)
	private Time horaInicio;
	
	@Column(name="hora_final", nullable = false)
	private Time horaFinal;

	public Turno(Long id, Time horaInicio, Time horaFinal) {
		super();
		this.id = id;
		this.horaInicio = horaInicio;
		this.horaFinal = horaFinal;
	}
	
	public Turno() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Time getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Time horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Time getHoraFinal() {
		return horaFinal;
	}

	public void setHoraFinal(Time horaFinal) {
		this.horaFinal = horaFinal;
	}

}
