package com.equipo3.reuneme.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.equipo3.reuneme.model.Administrador;

public interface AdministradorDAO extends JpaRepository<Administrador, String>{

    public Administrador findByEmail (String email);
	
}