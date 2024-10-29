package com.equipo3.reuneme.model;

public class Token {
	
	private static final int DURACION = 900000;
	private String id;
	private Usuario usuario;
	private String email;
	private long horaFin;
	
	public Token (String id, Usuario usu2, String email) {
		this.id = id;
		this.usuario = usu2;
		this.email = email;
		this.horaFin = System.currentTimeMillis() + DURACION;
		
	}
	
	public Token () {}
	
	public boolean caducado() {
		return System.currentTimeMillis()> this.horaFin;
	}

	public void incrementarTiempo() {
		this.horaFin = System.currentTimeMillis() + DURACION;
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public long getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(long horaFin) {
		this.horaFin = horaFin;
	}

	public static int getDuracion() {
		return DURACION;
	}

}