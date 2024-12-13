package com.equipo3.reuneme.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Empleados")
public class Empleado extends Usuario {

	@Column(length = 100)
	private String departamento;
	
	@Column(nullable = false)
	private LocalDate fechaalta;
	
	@Column(length = 100)
	private String perfil;
	
	@Column(nullable = false)
	private Boolean bloqueado;
	
	@Column(nullable = false)
	private Boolean verificado;
	
	
	
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	public LocalDate getFechaalta() {
		return fechaalta;
	}
	public void setFechaalta(LocalDate fechaalta) {
		this.fechaalta = fechaalta;
	}
	public String getPerfil() {
		return perfil;
	}
	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
	public boolean isBloqueado() {
		return bloqueado;
	}
	public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}
	public boolean isVerificado() {
		return verificado;
	}
	public void setVerificado(boolean verificado) {
		this.verificado = verificado;
	}
	
	public Empleado() {
	    this.setRole("ROLE_EMPLOYEE");
	}


}
