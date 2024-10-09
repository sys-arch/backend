package com.equipo3.reuneme.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Admins")
public class Administrador extends Usuario {
	
	@Column(nullable = false)
	private boolean interno;

	public boolean isInterno() {
		return this.interno;
	}

	public void setInterno(boolean interno) {
		this.interno = interno;
	}

}
