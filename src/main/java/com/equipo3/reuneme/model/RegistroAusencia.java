package com.equipo3.reuneme.model;

import java.util.Date;

public class RegistroAusencia {
	
	private String motivo;
	
	private Date fecha_inicio;
	
	private Date fecha_fin;

	public RegistroAusencia() {}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Date getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(Date fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	public Date getFecha_fin() {
		return fecha_fin;
	}

	public void setFecha_fin(Date fecha_fin) {
		this.fecha_fin = fecha_fin;
	}

}
