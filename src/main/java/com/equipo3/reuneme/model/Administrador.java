package com.equipo3.reuneme.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Admins")
public class Administrador extends Usuario {
	
	@Column(nullable = false)
	private Boolean interno;

	public Boolean isInterno() {
		return this.interno;
	}

	public void setInterno(boolean interno) {
		this.interno = interno;
	}
	public Administrador() {
	    this.setRole("ROLE_ADMIN");
	}


}
