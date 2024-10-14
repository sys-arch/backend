package com.equipo3.reuneme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.model.Administrador;
import com.equipo3.reuneme.model.RegistroAdmin;
import com.equipo3.reuneme.service.EmailService;
import com.equipo3.reuneme.service.PasswordService;
import com.equipo3.reuneme.service.UsuarioService;

@RestController
@RequestMapping("admins")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    UsuarioService userservice;

    @Autowired
    EmailService emailservice;
    
    @Autowired
    PasswordService pwdservice;

    // Registro de Usuario normal a.k.a Empleado
    @PostMapping("/register")
    public void registerAdmin(@RequestBody RegistroAdmin re) {
        // Comprobamos que ambas contraseñas son iguales
    	if(!this.pwdservice.isSamePwd(re.getPwd1(), re.getPwd2())) {
    		throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Las contraseñas no son iguales");
    	}
    	
    	// Comprobamos que la contraseña cumple requisitos de seguridad
    	if(!this.pwdservice.isValid(re.getPwd1())) {
    		throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, 
    				"Las contraseña no cumple con los requisitos de seguridad: "
    				+ "Entre 8 y 24 caracteres, Debe contener una maysucula, una minuscula, un digito, "
    				+ "un caracter especial y no debe contener espacios");
    	}
        

        // Comprobamos que el email tiene un formato válido
        if (!emailservice.validarEmail(re.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email insertado no tiene un formato válido: "
            		+ "usuario@dominio.com");
        }

        // Si pasa los controles, se registra en BD
        Administrador admin = new Administrador();
        admin.setEmail(re.getEmail());
        admin.setPwd(re.getPwd1());
        admin.setNombre(re.getNombre());
        admin.setApellido1(re.getApellido1());
        admin.setApellido2(re.getApellido2());
        admin.setCentro(re.getCentro());
        admin.setInterno(re.isInterno());
        
        this.userservice.registrarAdmin(admin);
    }
}
        
        
