package com.equipo3.reuneme.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.model.Administrador;
import com.equipo3.reuneme.model.Ausencia;
import com.equipo3.reuneme.model.Empleado;
import com.equipo3.reuneme.model.RegistroAdmin;
import com.equipo3.reuneme.model.RegistroAusencia;
import com.equipo3.reuneme.model.Turno;
import com.equipo3.reuneme.model.Usuario;
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
        if (!emailservice.validarEmail(re.getEmail().toLowerCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email insertado no tiene un formato válido: "
            		+ "usuario@dominio.com");
        }

        // Si pasa los controles, se registra en BD
        Administrador admin = new Administrador();
        admin.setEmail(re.getEmail().toLowerCase());
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
    public void modificarEmpleado(@RequestBody Empleado emp) {
    	try {
    		adminservice.actualizarEmpleado(emp.getEmail().toLowerCase(), emp);
    	} catch (Exception e) {
    		throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Error al modificar el empleado.");
    	}
	}
    
    /*********************************
     *VERIFICAR EMPLEADO
     ********************************/    
    @PutMapping("/verificarEmpleado")
    public void verificarEmpleado(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        email = email.toLowerCase();
        System.out.println(email);

        if (!this.emailservice.validarEmail(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El email no tiene un buen formato");
        }
        
        this.adminservice.verificarEmpleado(email);
    }

    /*********************************
     *BLOQUEAR/DESBLOQUEAR EMPLEADO
     ********************************/
    @PutMapping("/cambiarEstadoBloqueo")
    public void cambiarEstadoBloqueoEmpleado(@RequestParam String email, @RequestParam boolean bloquear) {
    	
    	email = email.toLowerCase();
    	
        if (!this.emailservice.validarEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email no tiene un formato válido: usuario@dominio.com");
        }
        this.adminservice.cambiarEstadoBloqueoEmpleado(email, bloquear);
    }

	    
    /*********************************
     *BORRAR EMPLEADO
     ********************************/
    @DeleteMapping("/borrarEmpleado")
    public void borrarUsuario(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        email = email.toLowerCase();
        
        if (email == null || email.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email es obligatorio");
        }
        
        this.adminservice.deleteUser(email);
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
    	
    	email = email.toLowerCase();
    	
    	if (!this.emailservice.validarEmail(email)) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El email no tiene un formato adecuado");
    	}
    	
    	return this.adminservice.getEmpleado(email);
    }
    
    /*********************************
     *CONSULTAR AUSENCIAS DE UN EMPLEADO
     ********************************/
	@PutMapping("/consultarAusencias")
	public List<Ausencia> getAusencias(@RequestParam String email) {
		
    	if (!this.emailservice.validarEmail(email)) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Revise el email, no es correcto");
    	}
		
	    return this.adminservice.consultarAusencias(email); 
	}
	
    /*********************************
     *AÑADIR AUSENCIA
     ********************************/
	@PutMapping("/anadirAusencia")
	public void anadirAusencia(@RequestParam String email, @RequestParam RegistroAusencia ausencia) {
		
    	if (!this.emailservice.validarEmail(email)) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Revise el email, no es correcto");
    	}
    	
    	if(Objects.isNull(ausencia)) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La ausencia que se intenta registrar está vacía");
    	}
		
	    adminservice.anadirAusencias(email, ausencia); 
	}
	
	@GetMapping("/all")
	@ResponseStatus(HttpStatus.OK)
	public List<Usuario> verTodosLosUsuarios() {
	    return this.adminservice.obtenerTodosLosUsuarios();
	}
	
	/*********************************
	 * VER DATOS DEL ADMINISTRADOR
	 ********************************/
	@GetMapping("/verDatos")
	public Administrador verDatosAdmin(@RequestParam String email) {
	    if (!this.emailservice.validarEmail(email)) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email no tiene un formato válido: usuario@dominio.com");
	    }
	    return this.adminservice.verDatos(email);
	}
	
    /*********************************
     *AÑADIR TURNO
     ********************************/
	@PostMapping("/anadirTurno")
	@ResponseStatus(HttpStatus.OK)
	public void anadirTurno( @RequestBody Turno t) {
    	if (Objects.isNull(t)) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Revise el email, no es correcto");
    	}
    	
    	this.adminservice.anadirTurno(t);
	}
	
    /*********************************
     *DEVOLVER TODOS LOS TURNOS
     ********************************/
	@PostMapping("/turnos")
	@ResponseStatus(HttpStatus.OK)
	public List<Turno> turnos( @RequestBody Turno t) {
    	if (Objects.isNull(t)) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Revise el email, no es correcto");
    	}
    	
    	return this.adminservice.turnos(t);
	}
	
	/*********************************
     *MODIFICAR ADMINISTRADOR
     ********************************/
    @PutMapping("/modificarAdministrador")
    public void modificarAdministrador(@RequestBody Administrador admin) {
    	try {
  
    		adminservice.actualizarAdministrador(admin.getEmail(), admin);
        
    	} catch (Exception e) {
    		throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Error al modifficar el empleado.");
    	}
	}
    
	/*********************************
     *COMPROBAR BLOQ./VALIDADO EMPLEADO
     ********************************/
    @PutMapping("/verificar")
    public boolean verificar(@RequestBody String email) {
    	
    	email = email.toLowerCase();
    	
	    if (!this.emailservice.validarEmail(email)) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email no tiene un formato válido: usuario@dominio.com");
	    }
	    return this.adminservice.comprobar(email);
	}

    /*********************************
     * OBTENER ROL DE USUARIO POR EMAIL
     ********************************/
    @GetMapping("/getUserRoleByEmail")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> getUserRoleByEmail(@RequestParam String email) {
        try {
            String role = adminservice.getUserRoleByEmail(email);
            Map<String, String> response = new HashMap<>();
            response.put("role", role); // Clave "role" con el valor del rol
            return response;
        } catch (ResponseStatusException e) {
            throw e; // Lanza la excepción tal cual para que el controlador maneje la respuesta HTTP
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener el rol del usuario.");
        }
    }


}
        
        
