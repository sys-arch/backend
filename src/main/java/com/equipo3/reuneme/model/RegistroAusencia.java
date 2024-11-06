package com.equipo3.reuneme.model;

import java.util.Date;

public class RegistroAusencia {
	
	private String motivo;
	
	private Date fechaInicio;
	
	private Date fechaFin;

	public RegistroAusencia() {}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fecha_inicio) {
		this.fechaInicio = fecha_inicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fecha_fin) {
		this.fechaFin = fecha_fin;
	}

}
