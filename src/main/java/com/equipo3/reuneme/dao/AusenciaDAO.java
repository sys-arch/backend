package com.equipo3.reuneme.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.equipo3.reuneme.model.Ausencia;

public interface AusenciaDAO extends JpaRepository<Ausencia, String> {
	
	Ausencia findbyIdUsuario(String idUsuario);

}
