package com.equipo3.reuneme.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.model.Empleado;
import com.equipo3.reuneme.service.EmailService;
import com.equipo3.reuneme.service.EmpleadoService;

@RestController
@RequestMapping(value = "/empleados")
@CrossOrigin(origins = "*", methods= {RequestMethod.PUT,RequestMethod.PUT})
public class EmpleadoController {

	@Autowired
	private EmpleadoService eservice;
	
	@Autowired
	private EmailService emailService;

	////////////////////////////////////
	// DEVOLVER INFORMACIÓN EMPLEADO
	////////////////////////////////////
	@PutMapping(value = "/verDatos")
	@ResponseStatus(HttpStatus.OK)
	public Empleado verDatos(@RequestBody String email) {
		
        if (!emailService.validarEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email insertado no tiene un formato válido: "
            		+ "usuario@dominio.com");
        }
		
		return this.eservice.verDatos(email);
	}
	
	
	////////////////////////////////////
	// ACTUALIZAR CONTRASEÑA
	////////////////////////////////////
	@PutMapping(value = "/actualizarPwd")
	@ResponseStatus(HttpStatus.OK)
	public void actualizarPwd(@RequestBody Map<String,String> info ) {
		
		String email = info.get("email").toString();
		String pwd = info.get("pwd").toString();
		
        if (!emailService.validarEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email insertado no tiene un formato válido: "
            		+ "usuario@dominio.com");
        }
		
		this.eservice.actualizarPwd(email, pwd);
	}
	
}
