package com.equipo3.reuneme.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.equipo3.reuneme.model.Administrador;

public interface AdministradorDAO extends MongoRepository<Administrador, String>{

    public Administrador findByEmail (String email);
	
}