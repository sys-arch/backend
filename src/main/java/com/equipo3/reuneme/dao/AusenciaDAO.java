package com.equipo3.reuneme.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.equipo3.reuneme.model.Ausencia;
import com.equipo3.reuneme.model.Empleado;

public interface AusenciaDAO extends JpaRepository<Ausencia, String> {
	
	public List<Ausencia> findAllByEmpleado(Empleado empleado);

}
