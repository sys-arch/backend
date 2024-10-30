package com.equipo3.reuneme.service;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.dao.EmpleadoDAO;
import com.equipo3.reuneme.model.Empleado;

@Service
public class EmpleadoService {
	@Autowired
	private EmpleadoDAO edao;
	
	
	public Empleado verDatos(String email) {
	    Empleado empleado = this.edao.findByEmail(email);
	    if (empleado == null) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado");
	    }
	    return empleado;
	}



	public void actualizarPwd(String email, String pwd) {
		Empleado e = this.edao.findByEmail(email);
		
		if(Objects.isNull(e)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El empleado no existe!");
		}
		
		this.edao.delete(e);
		pwd = org.apache.commons.codec.digest.DigestUtils.sha512Hex(pwd);
		e.setPwd(pwd);
		this.edao.save(e);
		
	}

}
