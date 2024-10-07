package com.equipo3.reuneme.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.model.DatosRegistro;
import com.equipo3.reuneme.model.Usuario;
import com.equipo3.reuneme.services.EmailService;
import com.equipo3.reuneme.services.UserService;


@RestController
@RequestMapping("users")
@CrossOrigin("*")
public class UsuarioController {

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;
	    
	    @PostMapping("/register")
	    public void register(@RequestBody DatosRegistro datos) {
	    	
	    	//Comprobamos que la contraseña cumple requisitos seguridad y ambas contraseñas son iguales
	    	datos.comprobarPwd();
	    	
	    	//Comprobamos que el email tiene un formato válido
	        if(emailService.validarEmail(datos.getEmail()) == false) {
	        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email insertado no tiene un formato válido");
	        }
	        
	        //Si pasa los controles, se registra en BD
	        Usuario user = new Usuario(datos);
	        this.userService.registrar(user);
	    }

}
