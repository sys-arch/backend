package com.equipo3.reuneme.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.equipo3.reuneme.model.Turno;

public interface TurnoDAO extends MongoRepository<Turno, String> {
	
}
