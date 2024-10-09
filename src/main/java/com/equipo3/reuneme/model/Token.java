package com.equipo3.reuneme.model;

public class Token {
	
	private final static int DURACION = 900000;
	private String id;
	private Usuario usuario;
	private long horaFin;
	
	public Token (String id, Usuario usu2) {
		this.id = id;
		this.usuario = usu2;
		this.horaFin = System.currentTimeMillis() + DURACION;
		
	}
	
	public String getId() {
		return id;

	}
	
	public boolean caducado() {
		return System.currentTimeMillis()> this.horaFin;
	}

	public void incrementarTiempo() {
		this.horaFin = System.currentTimeMillis() + DURACION;
		
	}

}