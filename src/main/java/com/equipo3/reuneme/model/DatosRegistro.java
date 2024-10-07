package com.equipo3.reuneme.model;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DatosRegistro {
	
	private String email;
	private String pwd1;
	private String pwd2;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String departamento;
	private String centro;
	private LocalDate fechaalta;
	private String perfil;
	
	public void comprobarPwd() {
		if(!pwd1.equals(pwd2)) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Las contraseñas no coinciden");
		}
		if (pwd1.length()<8) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Las contraseña tiene que tener 8 o más caracteres");
		}
		
        // Expresión regular para validar la contraseña
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pwd1);
        if(!matcher.matches()) {
        	throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Las contraseña no cumple con los requisitos de seguridad: "
        			+ "tener 1 mayuscula, 1 minuscula y 1 caracter especial");
        }
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPwd1() {
		return pwd1;
	}
	public void setPwd1(String pwd1) {
		this.pwd1 = pwd1;
	}
	public String getPwd2() {
		return pwd2;
	}
	public void setPwd2(String pwd2) {
		this.pwd2 = pwd2;
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
