package com.equipo3.reuneme.controller;

<<<<<<< Updated upstream
import java.util.HashMap;
=======
import java.util.List;
>>>>>>> Stashed changes
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.services.UsuarioService;

@RestController
@RequestMapping("users")
@CrossOrigin("*")
public class UsuarioController {
	
	@Autowired
	UsuarioService userservice;
	
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
	
	

}