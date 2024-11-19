package com.equipo3.reuneme.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.equipo3.reuneme.model.Reunion;

public interface ReunionDAO extends JpaRepository<Reunion, Long> {
	
	public List<Reunion> findByOrganizador(String organizador);

}
