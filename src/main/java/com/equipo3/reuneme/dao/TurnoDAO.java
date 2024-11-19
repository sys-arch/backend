package com.equipo3.reuneme.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.equipo3.reuneme.model.Turno;

public interface TurnoDAO extends JpaRepository<Turno, String> {
	
}
