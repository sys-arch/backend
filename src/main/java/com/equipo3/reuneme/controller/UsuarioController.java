package com.equipo3.reuneme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.model.Empleado;
import com.equipo3.reuneme.model.RegistroEmp;
import com.equipo3.reuneme.service.EmailService;
import com.equipo3.reuneme.service.UsuarioService;

import java.util.Map;
@RestController
@RequestMapping("users")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    UsuarioService userservice;

    @Autowired
    EmailService emailservice;
    
    @PostMapping("/login")
    public String login(@RequestBody Map<String, Object> userInfo) {
        // Llamamos al servicio para validar las credenciales y generar el token
        return userservice.login(userInfo);
    }

    // Registro de Usuario normal a.k.a Empleado
    @PostMapping("/register")
    public void register(@RequestBody RegistroEmp re) {
        // Comprobamos que la contraseña cumple requisitos de seguridad y ambas contraseñas son iguales
        re.comprobarPwd();

        // Comprobamos que el email tiene un formato válido
        if (!emailservice.validarEmail(re.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email insertado no tiene un formato válido");
        }

        // Si pasa los controles, se registra en BD
        Empleado emp = new Empleado();
        emp.setEmail(re.getEmail());
        emp.setPwd(re.getPwd1()); // Asignar la contraseña validada
        emp.setNombre(re.getNombre());
        emp.setApellido1(re.getApellido1());
        emp.setApellido2(re.getApellido2());
        emp.setCentro(re.getCentro());
        emp.setDepartamento(re.getDepartamento());
        emp.setPerfil(re.getPerfil());
        emp.setFechaalta(re.getFechaalta());
        emp.setBloqueado(re.isBloqueado());
        emp.setVerificado(re.isVerificado());

        this.userservice.registrar(emp);
    }
}
