package com.equipo3.reuneme.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.model.Administrador;
import com.equipo3.reuneme.model.Empleado;
import com.equipo3.reuneme.model.RegistroAdmin;
import com.equipo3.reuneme.service.AdminService;
import com.equipo3.reuneme.service.EmailService;
import com.equipo3.reuneme.service.PasswordService;

@RestController
@RequestMapping("/admins")
@CrossOrigin(origins = "*", methods= {RequestMethod.PUT,RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE})
public class AdminController {

    @Autowired
    EmailService emailservice;
    
    @Autowired
    PasswordService pwdservice;

    @Autowired
    AdminService adminservice;

    /*********************************
     *REGISTRO DE ADMINISTRADOR
     ********************************/
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
        
        this.adminservice.registrarAdmin(admin);
    }
    
    /*********************************
     *MODIFICAR EMPLEADO
     ********************************/
    @PutMapping("/modificarEmpleado")
    public void modificarEmpleado(@RequestBody Empleado empleadoActualizado) {
    	try {
    		adminservice.actualizarEmpleado(empleadoActualizado.getEmail(), empleadoActualizado);
    	} catch (Exception e) {
    		throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Error al modifficar el empleado.");
    	}
	}
    
    /*********************************
     *VERIFICAR EMPLEADO
     ********************************/    
    @PutMapping("/verificarEmpleado")
    public void verificarEmpleado(@RequestBody String email) {
    	
    	if (!this.emailservice.validarEmail(email)) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El email no tiene un buen formato");
    	}
    	
    	this.adminservice.verificarEmpleado(email);
	}
    
    /*********************************
     *DESBLOQUEAR EMPLEADO
     ********************************/
    @PutMapping("/desbloquearEmpleado")
    public void desbloquearEmpleado(@RequestBody String email) {
    	
    	if (!this.emailservice.validarEmail(email)) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El email es inválido");
    	}
    	
    	this.adminservice.desbloquearEmpleado(email);
	}
    
    /*********************************
     *BORRAR EMPLEADO
     ********************************/
	@DeleteMapping("/borrarEmpleado")
	public void delete(@RequestBody String email) {
		
    	if (!this.emailservice.validarEmail(email)) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El email es incorrecto");
    	}
    	
		this.adminservice.delete(email);
	}
  
    /*********************************
     *OBTENER LISTA DE EMPLEADOS
     ********************************/
    @GetMapping("/listaEmpleados")
    public List<Empleado> verEmpleados () {
    	return this.adminservice.getEmpleados();
    }
    
    /*********************************
     *OBTENER EMPLEADO
     ********************************/
    @PutMapping("/obtenerEmpleado")
    public Empleado verEmpleado (@RequestParam String email) {
    	
    	if (!this.emailservice.validarEmail(email)) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El email no tiene un formato adecuado");
    	}
    	
    	return this.adminservice.getEmpleado(email);
    }
    
    /*********************************
     *BLOQUEAR EMPLEADO
     ********************************/
	@PutMapping("/bloquearEmpleado")
	public void blockUser(@RequestParam String email) {
		
    	if (!this.emailservice.validarEmail(email)) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Revise el email, no es correcto");
    	}
		
	    adminservice.bloquear(email); 
	}

}
        
        
