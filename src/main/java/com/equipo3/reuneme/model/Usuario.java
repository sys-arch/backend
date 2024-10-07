package com.equipo3.reuneme.model;

import java.util.UUID;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity 
@Table(name = "users", indexes = {
	@Index(columnList = "email", unique = true),
	@Index(columnList = "pwd")
})

public class Usuario {
	
	@Id @Column(length = 36)
	private String id;
	@Column(length = 100, nullable = false)
	private String email;
	@Column(length = 100, nullable = false)
	private String pwd;
	@Column(length = 100, nullable = false)
	private String nombre;
	@Column(length = 100, nullable = false)
	private String apellido1;
	@Column(length = 100, nullable = false)
	private String apellido2;
	@Column(length = 50)
	private String departamento;
	@Column(length = 50, nullable = false)
	private String centro;
	@Column(length = 100, nullable = false)
	private LocalDate fechaalta;
	@Column(length = 250)
	private String perfil;
	
	public Usuario(DatosRegistro datos) {
		this.id = UUID.randomUUID().toString();
		this.email = datos.getEmail();
		this.pwd = datos.getPwd1();
		this.nombre = datos.getNombre();
		this.apellido1 = datos.getApellido1();
		this.apellido2 = datos.getApellido2();
		this.departamento = datos.getDepartamento();
		this.centro = datos.getCentro();
		this.fechaalta = datos.getFechaalta();
		this.perfil = datos.getPerfil();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getCentro() {
		return centro;
	}

	public void setCentro(String centro) {
		this.centro = centro;
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

}
