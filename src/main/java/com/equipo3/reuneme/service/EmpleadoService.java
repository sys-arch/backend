package com.equipo3.reuneme.service;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.dao.EmpleadoDAO;
import com.equipo3.reuneme.model.Empleado;

@Service
public class EmpleadoService {
	
	private EmpleadoDAO edao;
	
	
	public Empleado verDatos(String email) {
		return this.edao.findByEmail(email);
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
