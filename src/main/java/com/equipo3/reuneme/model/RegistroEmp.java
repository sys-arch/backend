package com.equipo3.reuneme.model;

import java.time.LocalDate;

public class RegistroEmp extends RegistroDatos {
	
	private String departamento;
	private LocalDate fechaalta;
	private String perfil;
	private boolean bloqueado;
	private boolean verificado;
//	private Ausencia ausencia;
	
	public RegistroEmp() {
	}
	
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
//	public Ausencia getAusencia() {
//		return ausencia;
//	}
//	public void setAusencia(Ausencia ausencia) {
//		this.ausencia = ausencia;
//	}
//	
	
}
