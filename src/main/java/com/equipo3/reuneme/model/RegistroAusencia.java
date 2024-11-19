package com.equipo3.reuneme.model;

import java.time.LocalDateTime;

public class RegistroAusencia {
	
	private String motivo;
	
	private LocalDateTime fechaInicio;
	
	private LocalDateTime fechaFin;

	public RegistroAusencia() {}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDateTime fecha_inicio) {
		this.fechaInicio = fecha_inicio;
	}

	public LocalDateTime getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDateTime fecha_fin) {
		this.fechaFin = fecha_fin;
	}

}
