package com.equipo3.reuneme.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.dao.EmpleadoDAO;
import com.equipo3.reuneme.model.Empleado;

@Service
public class AdminService {
	
	@Autowired
	private EmpleadoDAO empleadoDAO;

	public void actualizarEmpleado(String email, Empleado empleadoActualizado) {
		Empleado empleadoExistente = this.empleadoDAO.findByEmail(email);
		if(Objects.isNull(empleadoExistente)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No exisate el empleado seleccionado");
		}
		
		empleadoExistente.setNombre(empleadoActualizado.getNombre());
        empleadoExistente.setApellido1(empleadoActualizado.getApellido1());
        empleadoExistente.setApellido2(empleadoActualizado.getApellido2());
        empleadoExistente.setDepartamento(empleadoActualizado.getDepartamento());
        empleadoExistente.setPerfil(empleadoActualizado.getPerfil());
        empleadoExistente.setCentro(empleadoActualizado.getCentro());
        empleadoExistente.setBloqueado(empleadoActualizado.isBloqueado());
        empleadoExistente.setVerificado(empleadoActualizado.isVerificado());
        
        this.empleadoDAO.save(empleadoExistente);

	}

}
