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
import com.equipo3.reuneme.service.UsuarioService;

@RestController
@RequestMapping("admins")
@CrossOrigin("*")
public class AdminController {

    UsuarioService userservice;
    EmailService emailservice;
    Administrador admin;

    // Registro de Administrador
    @Autowired
    @PostMapping("/register")
    public void registerAdmin(@RequestBody RegistroAdmin re) {
        // Comprobamos que la contraseña cumple requisitos de seguridad y ambas contraseñas son iguales
        re.comprobarPwd();

        // Comprobamos que el email tiene un formato válido
        if (!emailservice.validarEmail(re.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email insertado no tiene un formato válido");
        }

        // Si pasa los controles, se registra en BD
        admin = new Administrador();
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
        
        
