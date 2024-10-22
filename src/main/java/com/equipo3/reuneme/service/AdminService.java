package com.equipo3.reuneme.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.dao.EmpleadoDAO;
import com.equipo3.reuneme.model.Empleado;

@Service
public class AdminService {
	
	@Autowired
	private EmpleadoDAO empdao;

	public void actualizarEmpleado(String email, Empleado empleadoActualizado) {
		Empleado empleadoExistente = this.empdao.findByEmail(email);
		if(Objects.isNull(empleadoExistente)) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No existe el empleado seleccionado");
		}
		
		empleadoExistente.setNombre(empleadoActualizado.getNombre());
        empleadoExistente.setApellido1(empleadoActualizado.getApellido1());
        empleadoExistente.setApellido2(empleadoActualizado.getApellido2());
        empleadoExistente.setDepartamento(empleadoActualizado.getDepartamento());
        empleadoExistente.setPerfil(empleadoActualizado.getPerfil());
        empleadoExistente.setCentro(empleadoActualizado.getCentro());
        empleadoExistente.setBloqueado(empleadoActualizado.isBloqueado());
        empleadoExistente.setVerificado(empleadoActualizado.isVerificado());
        
        this.empdao.save(empleadoExistente);

	}

	public void verificarEmpleado(String email) {
		Empleado emp = this.empdao.findByEmail(email);
		
		if(Objects.isNull(emp)) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No existe el empleado seleccionado");
		}
		
		if(emp.isVerificado()) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "El empleado ya está verificado");
		}
		
		this.empdao.deleteById(emp.getId());
		
		emp.setVerificado(true);
		emp.setBloqueado(false);
		
		this.empdao.save(emp);
		
	}

	public void desbloquearEmpleado(String email) {
		Empleado emp = this.empdao.findByEmail(email);
		
		if(Objects.isNull(emp)) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No existe el empleado seleccionado");
		}
		
		if(!emp.isBloqueado()) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "El empleado ya está desbloqueado");
		}
		
		this.empdao.deleteById(emp.getId());
		
		emp.setVerificado(true);
		emp.setBloqueado(false);
		
		this.empdao.save(emp);
		
	}

}
