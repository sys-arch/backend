package com.equipo3.reuneme.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.equipo3.reuneme.model.Ausencia;
import com.equipo3.reuneme.model.Usuario;

public interface AusenciaDAO extends MongoRepository<Ausencia, String> {
	
	public List<Ausencia> findByUsuario(Usuario usuario);

}
