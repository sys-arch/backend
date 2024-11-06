package com.equipo3.reuneme.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.equipo3.reuneme.model.Asistente;
import com.equipo3.reuneme.model.AsistenteId;

public interface AsistenteDAO extends JpaRepository<Asistente, AsistenteId> {
	List<Asistente> findByIdReunion(Long idReunion);
}
