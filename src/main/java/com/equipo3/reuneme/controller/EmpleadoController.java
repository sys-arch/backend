package com.equipo3.reuneme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.equipo3.reuneme.model.Empleado;
import com.equipo3.reuneme.service.UsuarioService;

@RestController
@RequestMapping(value = "/empleados")
public class EmpleadoController {

	@Autowired
	private UsuarioService userv;

	@GetMapping(value = "/empleado")
	@ResponseStatus(HttpStatus.OK)
	public Empleado employee(@RequestBody String email) {
		return this.userv.getEmpleado(email);
	}

	@PutMapping(value = "/actualizar")
	@ResponseStatus(HttpStatus.OK)
	public void actualizar(@RequestBody Empleado e) {
		this.userv.actualizar(e);
	}
	
}
