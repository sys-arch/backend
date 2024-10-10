package com.equipo3.reuneme.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.model.Empleado;
import com.equipo3.reuneme.model.RegistroEmp;
import com.equipo3.reuneme.service.EmailService;
import com.equipo3.reuneme.service.UsuarioService;

@RestController
@RequestMapping("users")
@CrossOrigin("*")
public class UsuarioController {
	
	UsuarioService userservice;
	EmailService emailservice;
	
	@Autowired
	@PutMapping("/login")
	public Map<String,String> login (@RequestBody Map<String, Object>info) {
		
		try {
			HashMap<String,String> resultado = new HashMap<>();
			resultado.put("token", this.userservice.login(info));
			resultado.put("activo", this.userservice.findActivo(info));
			return resultado;
		}catch(Exception e) {
			throw new ResponseStatusException (HttpStatus.CONFLICT);
		}
		
	}
	
	//Registro de Usuario normal a.k.a Empleado
	@Autowired
	@PostMapping("/register")
	public void register(@RequestBody RegistroEmp re) {

		// Comprobamos que la contraseña cumple requisitos seguridad y ambas contraseñas
		// son iguales
		re.comprobarPwd();

		// Comprobamos que el email tiene un formato válido
		if (!emailservice.validarEmail(re.getEmail())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email insertado no tiene un formato válido");
		}

		// Si pasa los controles, se registra en BD
		Empleado emp = new Empleado();
		this.userservice.registrar(emp);
	}
	
	@Autowired
	@PutMapping("/delete")
	public void delete (@RequestBody String email) {
		this.userservice.delete(email);
	}

}